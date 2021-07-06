package com.example.coffee
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.androidstudy.daraja.util.TransactionType
import com.example.coffee.adapters.buyadapter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_mpesa.*
import java.text.SimpleDateFormat
import java.util.*

class mpesa : AppCompatActivity(),MpesaListener{

    lateinit var daraja:Daraja
    private var ref:DatabaseReference?=null
    private var mDatabase:FirebaseDatabase?=null
    private var mAuth: FirebaseAuth?=null
    companion object {
        lateinit var mpesaListener: MpesaListener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpesa)

        mDatabase= FirebaseDatabase.getInstance()
        ref=mDatabase!!.reference!!.child("Transaction")
        mAuth= FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)

        var name:String=intent.getStringExtra("name")
        var cost:String=intent.getStringExtra("cost")
        var quantity:String=intent.getStringExtra("quantity")
        var counts:String=intent.getStringExtra("count")
        mpesanam.setText(name)
        mpesacos.setText(cost+" each")
        mpesaquantit.setText(quantity)

        var phones:String=intent.getStringExtra("phone")

        mpesaListener=this
        daraja = Daraja.with(
                "nbgKN6zNgU8dBpArX9BA9uSIKTG8SYkS",
                "oT1zDAU6FZLxZRZU",
                Env.SANDBOX,
                object : DarajaListener<AccessToken> {
                    override fun onResult(result: AccessToken) {
                       // Toast.makeText(this@mpesa, "MPESA TOKEN: ${result.access_token}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: String?) {
                        TODO("Not yet implemented")
                    }
                }
        )

        mpay.setOnClickListener {
            val count=mpesacoun.text.trim().toString().trim();
            if (count.isEmpty()) {
                mpesacoun.error = "Please enter number of bags"
                return@setOnClickListener}
            if (count>counts){
                mpesacoun.error="Try a small number of bags "+counts+" remaining"
                return@setOnClickListener
            }
            val lnmExpress = LNMExpress(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                    TransactionType.CustomerPayBillOnline,
                    "1",
                    phones,
                    "174379",
                    phones,
                    "https://us-central1-coffee-47280.cloudfunctions.net/api/myCallbackUrl",
                    "001ABC",
                    "Goods Payment"
            )
            daraja.requestMPESAExpress(lnmExpress,
                    object : DarajaListener<LNMResult> {
                        override fun onResult(result: LNMResult) {

                            FirebaseMessaging.getInstance()
                                    .subscribeToTopic(result.CheckoutRequestID.toString())

                            Toast.makeText(
                                    this@mpesa,
                                    result.ResponseDescription,
                                    Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(error: String?) {
                            Toast.makeText(this@mpesa, error, Toast.LENGTH_SHORT).show()
                        }
                    }
             )
        }
    }
    override fun sendFailed(reason: String) {
        runOnUiThread {
            Toast.makeText(
                    this, "Payment Failed\n" +
                    "Reason: $reason"
                    , Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun sendSuccessful(amount: String, phone: String, date: String, receipt: String) {
        runOnUiThread {
            var name:String=intent.getStringExtra("name")
            var cost:String=intent.getStringExtra("cost")
            var quantity:String=intent.getStringExtra("quantity")
            var itemid:String=intent.getStringExtra("itemid")
            val user=mAuth!!.currentUser
            val id=user!!.uid
            val count=mpesacoun.text.trim().toString().trim()
            val counts=count.toInt();
            val calendar = Calendar.getInstance()
            val tz = TimeZone.getDefault()
            val total=cost.toInt()*counts
            val totals=total.toString()
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val sdf = SimpleDateFormat("dd MMM YYYY", Locale.getDefault())
            val date = Date()
            val time = sdf.format(Date())
            val ref=FirebaseDatabase.getInstance().getReference("Transaction")
            val collector=Lipa(id,name,itemid,date.toString(),receipt,quantity, count,phone,totals)

            ref.child(receipt).setValue(collector).addOnCompleteListener{
                Toast.makeText(applicationContext, "Transaction saved successfully ", Toast.LENGTH_SHORT).show()
            }
            success()
        }
    }
    private fun success() {
        val dilog = Dialog(this)
        dilog.setContentView(R.layout.successful)
        dilog.show()
        val t = Timer()
        t.schedule(object : TimerTask() {
            override fun run() {
                dilog.dismiss()
                t.cancel()
            }
        }, 3000)
        dilog.setOnDismissListener(DialogInterface.OnDismissListener {
            finish()

        })
    }
}