package com.sidchiku9.studio42firstapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.lang.NumberFormatException


class MainActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    private var moistureReference: DatabaseReference? = null
    private var temperatureReference: DatabaseReference? = null
    private var humidityReference: DatabaseReference? = null
    private var dcStatus: DatabaseReference? = null
    private var dataAnalysisOne : DatabaseReference? = null
    private var dataAnalysisTwo : DatabaseReference? = null
    private var moistureLevel : String = ""
    private var temperature : String = ""
    private var moistureDA : Int = 0
    private var temperatureDA : Int = 0
    //this is a test comment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val turnOnbutton = findViewById<Button>(R.id.onButton)
        val turnOffbutton = findViewById<Button>(R.id.offButton)
        val moistureTextView = findViewById<TextView>(R.id.moistureUpdate)
        val temperatureTextView = findViewById<TextView>(R.id.temperatureUpdate)
        val humidityTextView = findViewById<TextView>(R.id.humidityUpdate)
        val dcMotorStatus = findViewById<TextView>(R.id.dcMotorStatus)
        val suggestionsUpdate = findViewById<TextView>(R.id.suggestionUpdate)

        //DC MOTOR

        mDatabase = FirebaseDatabase.getInstance().reference

        turnOnbutton.setOnClickListener {
            mDatabase!!.child("DC Motor Status").setValue(1)
        }
        turnOffbutton.setOnClickListener {
            mDatabase!!.child("DC Motor Status").setValue(0)
        }

        //DC MOTOR READ VALUE

        dcStatus = FirebaseDatabase.getInstance().getReference("DC Motor Status")

        dcStatus?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dcStatus = snapshot.value.toString()

                if(dcStatus == "0"){
                    dcMotorStatus.text = "Motor is OFF"
                }
                else{
                    dcMotorStatus.text = "Motor is ON"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //SOIL MOISTURE SENSOR

        moistureReference = FirebaseDatabase.getInstance().getReference("Soil Moisture Sensor")

        moistureReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                moistureLevel = snapshot.value.toString()
                moistureTextView.text = moistureLevel
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Error")
            }

        })

        //TEMPERATURE SENSOR

        temperatureReference = FirebaseDatabase.getInstance().getReference("Temperature Sensor")

        temperatureReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                temperature = snapshot.value.toString()
                temperatureTextView.text = temperature
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Error")
            }

        })

        //HUMIDITY SENSOR

        humidityReference = FirebaseDatabase.getInstance().getReference("Humidity Sensor")

        humidityReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val text : String = snapshot.value.toString()
                humidityTextView.text = text
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //SUGGESTIONS DATA ANALYSIS PART
        dataAnalysisOne = FirebaseDatabase.getInstance().getReference("Soil Moisture Sensor")
        dataAnalysisTwo = FirebaseDatabase.getInstance().getReference("Temperature Sensor")

        dataAnalysisOne?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                moistureDA = (snapshot.value as Long).toInt()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        dataAnalysisTwo?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                temperatureDA = (snapshot.value as Long).toInt()

                if(temperatureDA <= 25 && moistureDA >= 80){
                    suggestionsUpdate.text = "Ideal Temp and Moisture. The field is in an ideal condition. Maintain this to expect good yield."
                }
                else if(temperatureDA >= 30 && moistureDA <= 75){
                    suggestionsUpdate.text = "Low moisture. Please water the fields."
                }
                else{
                    suggestionsUpdate.text = "Please refresh the app."
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}