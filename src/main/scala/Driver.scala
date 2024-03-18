import java.sql.Timestamp
import java.text.SimpleDateFormat

import scala.io.StdIn

import com.facebook.flowframe.Policy
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}

import scala.annotation.tailrec

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.HBox
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.text.Text

import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.stage.Screen
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.text.Font


object Wrapper {
	
	
	def joinWithWrapper[T,U](db1 : Dataset[Calendar], db2 : Dataset[ChessEvent], col1 : String, col2 : String, foo: (Calendar, ChessEvent) => Boolean) : Dataset[(Calendar, ChessEvent)] = {
	
	val cal : ChessEvent = db2.take(2)(1)
	val chess : Calendar = db1.take(2)(1)
	val r : Boolean = foo(chess, cal)
	println(r)
	var ds : Dataset[(Calendar, ChessEvent)] = {null}
	if(r || col1 == "name" && col2 == "player2") {
		ds = db1.joinWith(db2, db1("name") === db2("player2"), "inner")
		println("name with player2 has been compared")
	}
	else if(r || col1 == "id" && col2 == "id") {
		ds = db1.joinWith(db2, db1("id") === db2("id"), "inner")
		println("id with id has been compared")
	}
	else if(r || col1 == "description" && col2 == "description") {
		ds = db1.joinWith(db2, db1("description") === db2("description"), "inner")
		println("description with description has been compared")
	}
	else if(r || col1 == "start_time" && col2 == "start_time") {
		ds = db1.joinWith(db2, db1("start_time") === db2("start_time"), "inner")
		println("start_time with start_time has been compared")
	}
	else if(r || col1 == "end_time" && col2 == "end_time") {
		ds = db1.joinWith(db2, db1("end_time") === db2("end_time"), "inner")
		println("end_time with end_time has been compared")
	}
	else if(r || col1 == "user_id" && col2 == "id") {
		ds = db1.joinWith(db2, db1("user_id") === db2("id"), "inner")
		println("user_id with id has been compared")
	}
	else if(r || col1 == "user_name" && col2 == "player1") {
		ds = db1.joinWith(db2, db1("user_name") === db2("player1"), "inner")
		println("user_name with user_name has been compared")
	}
	else if(r || col1 == "name" && col2 == "player1") {
		ds = db1.joinWith(db2, db1("name") === db2("player1"), "inner")
		println("name with player1 has been compared")
	}
	else if(r || col1 == "user_name" && col2 == "player2") {
		ds = db1.joinWith(db2, db1("user_name") === db2("player2"), "inner")
		println("user_name with player2 has been compared")
	}
	else if(r || col1 == "name" && col2 == "name") {
		ds = db1.joinWith(db2, db1("name") === db2("name"), "inner")
		println("name with name has been compared")
	}
	else if(r || col1 == "user_name" && col2 == "name") {
		ds = db1.joinWith(db2, db1("user_name") === db2("name"), "inner")
		println("user_name with name has been compared")
	}
	else {
		println("none of the comparisons work")
	}
	ds
  }
	
}

//Test 1 (no policies - Flow Frame accepts this code, works as expected)	
/*object LambdaTest {

  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int , Int) => Boolean) : Boolean = {
    var result : Boolean = false
    val num : Int = 1 
    val temp : Int = 1
    result = compareTwoArg(num, temp)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/

//Test 2 (same policy for both integers - Flow Frame accepts this code, works as expected)
/*object LambdaTest {

  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int,Int) => Boolean) : Boolean = {
    var result : Boolean = false
   
    val num : Int @Policy("secret") = 1
    val temp : Int @Policy("secret") = 1 
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/


//Test 3 (secret for one integer and any for the other integer - Flow Frame does not accept this code, does not work as expected)
/*object LambdaTest {

  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int,Int) => Boolean) : Boolean = {
    var result : Boolean = false
   
    val num : Int @Policy("any") = 1
    val temp : Int @Policy("secret") = 1 
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/


//Test 4 (secret for one integer and no policy for the other integer - Flow Frame does accept this code, does work as expected)
/*object LambdaTest {

  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int,Int) => Boolean) : Boolean = {
    var result : Boolean = false
   
    val num : Int = 1
    val temp : Int @Policy("secret") = 1 
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/


//Test 5 (secret for one integer and bob for the other integer - Flow Frame does not accept this code, does not work as expected)
/*object LambdaTest {
 
  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int,Int) => Boolean) : Boolean = {
    var result : Boolean = false
   
    val num : Int @Policy("bob") = 1
    val temp : Int @Policy("secret") = 1 
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/



//Test 6 (secret for one integer and bob for the other integer with the result set as bob and secret - Flow Frame does not accept this code, does not work as expected)
/*object LambdaTest {

  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int,Int) => Boolean) : Boolean = {
    var result : Boolean @Policy("bob and secret") = false
   
    val num : Int @Policy("bob") = 1
    val temp : Int @Policy("secret") = 1 
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/


//Test 7 (secret for one integer and bob for the other integer with the result set as bob and secret - Flow Frame does not accept this code, does not work as expected)
/*object LambdaTest {
 
  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int,Int) => Boolean) : Boolean = {
    var result : Boolean @Policy("bob and secret") = false
    val temp : Int @Policy("secret") = 1
    val num : Int @Policy("bob") = 1
     
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/

//Test 8 (secret for one integer and bob for the other integer with the result set as bob and secret - Flow Frame does not accept this code, does not work as expected)
/*object LambdaTest {

  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int,Int) => Boolean) : Boolean = {
    var result : Boolean @Policy("bob and secret") = false
    
    val num : Int @Policy("secret") = 1
    val temp : Int @Policy("bob") = 1
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/

//Test 9 (secret for one integer and bob for the other integer with the result set as bob and secret - Flow Frame does not accept this code, does not work as expected)
/*object LambdaTest {

  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int,Int) => Boolean) : Boolean = {
    var result : Boolean @Policy("bob and secret") = false
    
    val temp : Int @Policy("bob") = 1
    val num : Int @Policy("secret") = 1
    result = compareTwoArg(num, temp)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/

//Test 10 (secret for one integer and 'bob and secret' for the other integer with the result set as bob and secret - Flow Frame does accept this code, does work as expected)
/*object LambdaTest {
 
  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int,Int) => Boolean) : Boolean = {
    var result : Boolean @Policy("bob and secret") = false
    
    val num : Int @Policy("bob and secret") = 1
    val temp : Int @Policy("secret") = 1
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/
	
//Test 11 (secret for one integer and bob for the other integer with the result set as bob and secret - Flow Frame does not accept this code, does not work as expected)
/*object LambdaTest {
 
  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int @Policy("bob"),Int @Policy("secret")) => Boolean @Policy("bob and secret")) : Boolean = {
    var result : Boolean @Policy("bob and secret") = false
    
    val num : Int @Policy("bob") = 1
    val temp : Int @Policy("secret") = 1
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/

//Test 12 (secret for one integer and 'bob and secret' for the other integer with the result set as bob and secret - Flow Frame does accept this code, does work as expected)
/*object LambdaTest {
 
  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int @Policy("bob and secret"),Int @Policy("secret")) => Boolean @Policy("bob and secret")) : Boolean = {
    var result : Boolean @Policy("bob and secret") = false
    
    val num : Int @Policy("bob and secret") = 1
    val temp : Int @Policy("secret") = 1
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res = testWrapper(compareTwo)
  }

}*/

//Test 13 (secret for one integer and bob for the other integer with the result set as bob and secret - Flow Frame does not accept this code, does not work as expected)
/*object LambdaTest {
 
  def compareTwo(a : Int, b : Int) : Boolean ={
    a == b
  }

  
  def testWrapper(compareTwoArg: (Int @Policy("bob"),Int @Policy("secret")) => Boolean @Policy("bob and secret")) : Boolean @Policy("bob and secret") = {
    var result : Boolean @Policy("bob and secret") = false
    
    val num : Int @Policy("bob") = 1
    val temp : Int @Policy("secret") = 1
    result = compareTwoArg(temp, num)
    result
  }

  def apply(spark:SparkSession): Unit = {
	var res : Boolean = false
    res @Policy("bob and secret") = testWrapper(compareTwo)
  }

}*/


object Driver extends JFXApp{
  stage = new PrimaryStage {
    title = "Safe Scheduler"
    width = 900
    height = 600

	val borderP = new BorderPane()

    val contentPane = new VBox()
	
	val r = new Text()
	r.setText("Safe Scheduler!")
	r.setFont(Font.font("Ariel", 30))
	r.setFill(Color.GREEN)

	val label1 = new Label("Welcome to Safe Scheduler! This app allows two")
	val label2 = new Label("players to find a 30-minute time slot to set-up a")
	val label3 = new Label("Chess Challenge. The fun thing is that it does so")
	val label4 = new Label("*truly* securely!")


	val label6 = new Label(" ")

	
	val l3 = new Label("Please enter the date in (yyyy-MM-dd) format when")
	val label5 = new Label("you would like to set-up a chess match:")
	val b3 = new TextField()
	b3.maxWidth = 100
	b3.maxHeight = 100
	b3.id = "Date"
	b3.text = "2024-03-04"
	
	
	val l1 = new Label("Player 1 Name:")
    val b1 = new TextField()
    b1.maxWidth = 100
    b1.maxHeight = 100
    b1.id = "Player1"
    b1.text = "Anand"
    

    val l2 = new Label("Player 2 Name:")
    val b2 = new TextField()
	b2.maxWidth = 100
	b2.maxHeight = 100
	b2.id = "Player2"
	b2.text = "Prag"
	
	val enter = new Button()
	enter.text = "Enter"
	
    
    contentPane.children.add(r)

	contentPane.children.add(label1)
	contentPane.children.add(label2)
	contentPane.children.add(label3)
	contentPane.children.add(label4)
	
	contentPane.children.add(label6)

	contentPane.children.add(l3)
	contentPane.children.add(label5)
	contentPane.children.add(b3)
	
	
	contentPane.children.add(l1)
    contentPane.children.add(b1)
    
    
    contentPane.children.add(l2)
    contentPane.children.add(b2)
    
    contentPane.children.add(enter)
    borderP.setLeft(contentPane)
    scene = new Scene (borderP)
    
    
	enter.onAction = (event : ActionEvent) => {
		
		val spark = SparkSession.builder.appName("SafeScheduler")
		.master("local[1]")
		.getOrCreate()
		spark.sparkContext.setLogLevel("ERROR")
		
		val proposedMeetingDate = b3.text.value
		val player1 = b1.text.value
		val player2 = b2.text.value
		
		val calendarTable = Calendar(spark)
		val inviteeEvents = calendarTable.filter(ev => ev.user_name == player1 || ev.user_name == player2)
		val conflictFreeTimes = findSuitableTime(inviteeEvents, spark, proposedMeetingDate)
		
		val label5 = new Label("Here is a list of 30-minute slots (between 9 AM and 11 PM)")
		val ll5 = new Label("where both player 1 and player 2 are available:")
		contentPane.children.add(label5)
		contentPane.children.add(ll5)
		val texta = new TextArea()
		texta.maxWidth = 200
		contentPane.children.add(texta)
		var temp: String = ""
		
		var c = conflictFreeTimes
		var i = 1
		while(c != Nil) {
			
			temp += "[" + i + "] " + calendarToString(c.head)
			c = c.drop(1)
			i = i + 1
		}
		texta.text.value = temp
		val rightPane = new VBox()
		borderP.setCenter(rightPane)
		
		var label11 = new Label("Now, enter the index of the meeting start time (ast it appears")
		var l6 = new Label("in the square bracket to the left) to set up the challenge:")
		l6.setWrapText(true)
		
		rightPane.children.add(label11)
		rightPane.children.add(l6)
		val l7 = new Label("Meeting Index")
		val b7 = new TextField()
		b7.maxWidth = 100;
		b7.maxHeight = 100;
		b7.id = "Index"
		b7.text = "1"
		
		rightPane.children.add(l7)
		rightPane.children.add(b7)
		
		
		val l8 = new Label("Meeting Name (Optional)")
		val b8 = new TextField()
		b8.maxWidth = 300;
		b8.id = "Name"
		b8.text = ""
		
		val l10 = new Label("Meeting Description (Optional)")
		val b10 = new TextField()
		b10.maxWidth = 300;
		b10.minHeight = 100;
		b10.id = "Description"
		b10.text = ""
		
		rightPane.children.add(l8)
		rightPane.children.add(b8)
		rightPane.children.add(l10)
		rightPane.children.add(b10)
		
		
		val ce = new Button()
		ce.text = "Create event"
		rightPane.children.add(ce)
		ce.onAction = (event : ActionEvent) => {
			
			val chessEventDs = ChessEvent(spark)
			val meetingIndex = b7.text.value.toInt
			val meetingName = b8.text.value
			val meetingDescription = b10.text.value
			createEvent(spark, calendarTable, conflictFreeTimes, meetingIndex, meetingName, meetingDescription, player1, player2, chessEventDs, rightPane)
			
		}
	
	}

  }



  //@tailrec - not a recursive function anymore, error when recursive function is used
  def printAvailableTimes(calendars: List[java.util.Calendar], index: Int): Unit = {

    var c = calendars
    var i = index
    while(c != Nil) {
        print("[" + i + "] " + calendarToString(c.head))
        c = c.drop(1)
        i = i + 1
        print("")
   }
  }

  def createEvent(sparkSession: SparkSession, event: Dataset[Calendar], conflictFreeTimes: List[java.util.Calendar],
                  meetingIndex: Int, meetingName: String = "", meetingDescription: String = "", player1: String, player2: String,
                  chessEvent: Dataset[ChessEvent], rightPane: VBox): Unit = {
    import sparkSession.implicits._
    import org.apache.spark.sql.functions.col


    val startTs = new Timestamp(conflictFreeTimes(meetingIndex - 1).getTimeInMillis)
    val endTsCal = java.util.Calendar.getInstance()
    endTsCal.setTimeInMillis(startTs.getTime)
    endTsCal.add(java.util.Calendar.MINUTE, 30)
    val endTs = new Timestamp(endTsCal.getTimeInMillis)
    val max = chessEvent.agg(org.apache.spark.sql.functions.max(col("id"))).collect()(0)(0).asInstanceOf[Int]
    val player1Event = event.filter(ev => ev.user_name == player1).first()
    val player2Event = event.filter(ev => ev.user_name == player2).first()
    val meetingId = max + 1
    val player1Name = player1Event.user_name
    val player2Name = player2Event.user_name
    val chessMeetingName = "[Chess-Event] " + (if (meetingName == "") (player1Name + " - " + player2Name) else meetingName)
    val chessMeetingDescription = "[Chess-Event] " + meetingDescription

    // happy flow
    val newChessEvent = new ChessEvent(meetingId, player1Name, player2Name, chessMeetingName, chessMeetingDescription,
      startTs, endTs)


    // malicious example
//    val player2LectureEvent = event.filter(ev => ev.user_name == player2Name
//      && ev.name.contains("lecture")).first()
//    var modifiedStartTime = new Timestamp(System.currentTimeMillis())
//    modifiedStartTime = player2LectureEvent.end_time
//    val modifiedEndTsCal = java.util.Calendar.getInstance()
//    modifiedEndTsCal.setTimeInMillis(modifiedStartTime.getTime)
//    modifiedEndTsCal.add(java.util.Calendar.MINUTE, 30)
//    val modifiedEndTs = new Timestamp(modifiedEndTsCal.getTimeInMillis)
//    val newChessEvent = new ChessEvent(meetingId, player1Name, player2Name,
//      chessMeetingName, chessMeetingDescription,
//      modifiedStartTime, modifiedEndTs)

    // diamond-flow
//    var modifiedStartTime = new Timestamp(System.currentTimeMillis())
//    modifiedStartTime = if (StdIn.readLine() == "got_you_compiler!")
//      (event.filter(ev => ev.user_name == player2Name
//        && ev.name.contains("lecture")).first()).end_time else startTs
//    val modifiedEndTsCal = java.util.Calendar.getInstance()
//    modifiedEndTsCal.setTimeInMillis(modifiedStartTime.getTime)
//    modifiedEndTsCal.add(java.util.Calendar.MINUTE, 30)
//    val modifiedEndTs = new Timestamp(modifiedEndTsCal.getTimeInMillis)
//    val newChessEvent = new ChessEvent(meetingId, player1Name, player2Name,
//        chessMeetingName, chessMeetingDescription,
//        modifiedStartTime, modifiedEndTs)



    val newRow = Seq(newChessEvent).toDS()
    newRow.write
              .mode(SaveMode.Append)
              .format("jdbc")
              .option("driver","com.mysql.cj.jdbc.Driver")
              .option("url", "jdbc:mysql://localhost:3306/safe_scheduler")
              .option("dbtable", "ChessEvent")
              .option("user", "root")
              .option("password", "")
              .save()
              
	val ll10 = new Label("Event created successfully. Event details:")
	val ll12 = new Label("Event name: " + chessMeetingName)
	rightPane.children.add(ll10)	 
	rightPane.children.add(ll12)
	
              
    //val res : Dataset[(Calendar, ChessEvent)] = joinWithWrapper(event, chessEvent, "name", "player2", f) //PolicyStructMismatchException is being thrown here

  }
  
  def f(T1 : Calendar, U1 : ChessEvent) : Boolean = { T1.name == U1.player2 }
  def f2(T1 : Calendar, U1 : ChessEvent) : Boolean = { T1.id == U1.id }
  def f3(T1 : Calendar, U1: ChessEvent) : Boolean = {T1.description == U1.description }
  def f4(T1 : Calendar, U1: ChessEvent) : Boolean = {T1.start_time == U1.start_time }
  def f5(T1 : Calendar, U1: ChessEvent) : Boolean = {T1.end_time == U1.end_time }
  def f6(T1 : Calendar, U1: ChessEvent) : Boolean = {T1.user_id == U1.id }
  def f7(T1 : Calendar, U1: ChessEvent) : Boolean = {T1.user_name == U1.player1 }
  def f8(T1 : Calendar, U1: ChessEvent) : Boolean = {T1.name == U1.player1 }
  def f9(T1 : Calendar, U1: ChessEvent) : Boolean = {T1.user_name == U1.player2 }
  def f10(T1 : Calendar, U1: ChessEvent) : Boolean = {T1.name == U1.name }
  def f11(T1 : Calendar, U1: ChessEvent) : Boolean = {T1.user_name == U1.name }
  
  
  def calendarToString(cal: java.util.Calendar): String = {
    val amOrPm = if (cal.get(java.util.Calendar.AM_PM) == 0) "AM" else "PM"
    val hour = if(cal.get(java.util.Calendar.HOUR) == 0) "12" else cal.get(java.util.Calendar.HOUR)
    val minute = if(cal.get(java.util.Calendar.MINUTE) == 0) "00" else cal.get(java.util.Calendar.MINUTE)
          hour + ":" +
            minute + " " + amOrPm + "\n"
  }

  // creates a calendar list starting at 9 am and ending at 11 pm
  //@tailrec - not a recursive function anymore, error when using recursive function
  def createStaticCalendar(list: List[java.util.Calendar], dateTokens: Array[String], minuteCounter: Int): List[java.util.Calendar] = {
    if(list.length == 29)
      list
    else {
      var l = list
      var m = minuteCounter
      while(l.length != 29) {
        val cal = java.util.Calendar.getInstance()
        cal.set(Integer.parseInt(dateTokens(0)), Integer.parseInt(dateTokens(1)) - 1,
          Integer.parseInt(dateTokens(2)), 9, 0, 0)
        cal.add(java.util.Calendar.MINUTE, m)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        // making 30 minute strides
        l = cal :: l
        m = m + 30
      }
      l
    }
  }

  // use fold here
  //@tailrec - not a recursive function anymore, error when using recursive function
  def getCalendarList(event: List[Calendar], calendarList: List[java.util.Calendar]): List[java.util.Calendar] = {

    var r = event
    var s = calendarList
    while (r != Nil) {
          val startTime = java.util.Calendar.getInstance()
          startTime.setTimeInMillis(r.head.start_time.getTime)
          val endTime = java.util.Calendar.getInstance()
          endTime.setTimeInMillis(r.head.end_time.getTime)
          // fold:
          val conflictFreeCalendarList = removeConflict(startTime, endTime, s, List()).reverse
          r = r.drop(1)
          s = conflictFreeCalendarList
    }
    s
  }


  // returns all possible 30-minute meeting slots with no conflict between player 1 and player 2
  def findSuitableTime(event: Dataset[Calendar], sparkSession: SparkSession, proposedMeetingDate: String):
  List[java.util.Calendar] = {
    val inviteeEventsOnMeetingDate = event.filter(event => dateToString(event.start_time.getTime) == proposedMeetingDate)
    var calendarList: List[java.util.Calendar] = List()
    val dateTokens = proposedMeetingDate.split("-")
    calendarList = createStaticCalendar(calendarList, dateTokens, 0).reverse
    import scala.collection.JavaConverters._

    val eventsCollected = inviteeEventsOnMeetingDate.collectAsList()     //line below throws an error
    //val eventsCollected = inviteeEventsOnMeetingDate.takeAsList(1)
   val eventsCollectedList = eventsCollected.asScala.toList
    // for each event, iterate through the static calendar list and remove items from it if a conflict exists.
    getCalendarList(eventsCollectedList, calendarList)
      }

  def dateToString(timestamp: Long): String = {
    val date = new java.util.Date(timestamp)
    new SimpleDateFormat("yyyy-MM-dd").format(date)
  }

  def dateToCompleteString(timestamp: Long): String = {
    val date = new java.util.Date(timestamp)
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
  }

  // use filter here
  //@tailrec - not a recursive function anymore, error when using recursive function
  def removeConflict(startTime:java.util.Calendar,
                             endTime: java.util.Calendar, calendarList: List[java.util.Calendar], acc: List[java.util.Calendar]):
  List[java.util.Calendar] = {
    //    println("Attempting to remove conflicts from calendarList")
    //    println("Starting...")
    //    val filteredCalendarDs = calendarList.filter(cal => {
    //      val strideHead = cal
    //      val nextStride = Calendar.getInstance()
    //      nextStride.setTimeInMillis(cal.getTimeInMillis)
    //      nextStride.add(Calendar.MINUTE, 30)
    //      // cond1: if stridehead is before start time then nextStride should be on or before the start time too - otherwise there's a conflict
    //      // cond2: if strideHead is after the end time, then there's no conflict
    //      (strideHead.before(startTime) && !nextStride.after(startTime)) || strideHead.after(endTime)
    //    })
    //    println("Removed conflicts from calendarList")
    //    println("Exiting...")
    //    filteredCalendarDs
    //  }
    var s = calendarList
    var a = acc
    while (s.length > 1) {
          var nextStride = s(1)
          val difference = s(1).getTimeInMillis - s.head.getTimeInMillis
          val diffInMins = difference / (60 * 1000)
          if (diffInMins > 30) {
            nextStride = java.util.Calendar.getInstance()
            nextStride.setTimeInMillis(s.head.getTimeInMillis)
            nextStride.add(java.util.Calendar.MINUTE, 30)
          }
          if ((s.head.before(startTime) && nextStride.after(startTime)) ||
            !s.head.before(startTime) && s.head.before(endTime)) {
            // conflict exists, dont add this strideHead instance to the acc
            s = s.drop(1)
          } else {
            // no conflict with this strideHead
            a = s.head :: a
            s = s.drop(1)
          }
    }
    if(s.length == 1) {
      s.head :: a
    }
    else {
      a
    }
  }
}

