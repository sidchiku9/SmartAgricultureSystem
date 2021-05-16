package com.sidchiku9.studio42firstapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    private var moistureReference: DatabaseReference? = null
    private var temperatureReference: DatabaseReference? = null
    private var humidityReference: DatabaseReference? = null
    private var dcStatus: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val turnOnbutton = findViewById<Button>(R.id.onButton)
        val turnOffbutton = findViewById<Button>(R.id.offButton)
        val moistureTextView = findViewById<TextView>(R.id.moistureUpdate)
        val temperatureTextView = findViewById<TextView>(R.id.temperatureUpdate)
        val humidityTextView = findViewById<TextView>(R.id.humidityUpdate)
        val dcMotorStatus = findViewById<TextView>(R.id.dcMotorStatus)

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
                val text : String = snapshot.value.toString()
                moistureTextView.text = text
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //TEMPERATURE SENSOR

        temperatureReference = FirebaseDatabase.getInstance().getReference("Temperature Sensor")

        temperatureReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val text : String = snapshot.value.toString()
                temperatureTextView.text = text
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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
    }
}