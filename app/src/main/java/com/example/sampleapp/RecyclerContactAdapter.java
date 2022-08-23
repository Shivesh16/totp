package com.example.sampleapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.Random;

import de.taimos.totp.TOTP;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder > {

    Context context;
    ArrayList<ContactModel> arrContacts;

    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    RecyclerContactAdapter(Context context, ArrayList<ContactModel> arrContacts){
        this.context = context;
        this.arrContacts = arrContacts;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.serviceprovider,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {



        ContactModel model = (ContactModel) arrContacts.get(position);
        holder.imagecontact.setImageResource(model.img);
        holder.txtname.setText(arrContacts.get(position).name);
        holder.txtnum.setText(arrContacts.get(position).number);

        holder.realmodify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_update_lay);


                EditText edtname = dialog.findViewById(R.id.edtname);
                EditText edtnumber = dialog.findViewById(R.id.edtnumber);
                Button btnadd = dialog.findViewById(R.id.btnadd);

                TextView txtTitle = dialog.findViewById(R.id.txtTitle);

                edtname.setText((arrContacts.get(position)).name);
                edtnumber.setText((arrContacts.get(position)).number);


                txtTitle.setText("Modify");
                btnadd.setText("Modify");
                btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String name="",number="";
                        if(!edtname.getText().toString().equals("")){
                            name = edtname.getText().toString();
                        } else {
                            Toast.makeText(context,"please enter contact name!",Toast.LENGTH_SHORT).show();
                        }
                        if (!edtnumber.getText().toString().equals("")){
                            number = edtnumber.getText().toString();
                        } else {
                            Toast.makeText(context,"please enter contact number!",Toast.LENGTH_SHORT).show();
                        }
                        arrContacts.set(position, new ContactModel(R.drawable.ic_launcher_foreground,name,number));
                        notifyItemChanged(position);

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        holder.llRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.displaytotp);

                TextView txtviewtotp;
                txtviewtotp=dialog.findViewById(R.id.txtviewtotp);
                Random random = new Random();
                String secretKey="KA54QGQSZZVBUFIH3XACOAW2VNFKXXVV";
                txtviewtotp.setText(""+getTOTPCode(secretKey));
                System.out.println(getTOTPCode(secretKey));
//                random.nextInt(100000-10000)+1
                TextView timer;
                timer= dialog.findViewById(R.id.timer);
                new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long l) {
                        timer.setText( ""+l/1000);

                    }

                    @Override
                    public void onFinish() {
//                        txtviewtotp.setText(""+random.nextInt(1000000-100000)+1);
                        dialog.dismiss();
                    }
                }.start();

                dialog.show();
            }
        });

        holder.btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete this?")
                        .setIcon(R.drawable.ic_delete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                arrContacts.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtname, txtnum;
        ImageView imagecontact;
        LinearLayout llRow;
        Button btndel;
        Button realmodify;
        TextView timer;
        TextView txtviewtotp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtname = itemView.findViewById(R.id.txtname);
            txtnum = itemView.findViewById(R.id.txtnum);
            imagecontact = itemView.findViewById(R.id.imgcontact);
            llRow = itemView.findViewById(R.id.llrow);
            btndel = itemView.findViewById(R.id.btndel);
            realmodify = itemView.findViewById(R.id.realmodify);
            timer = itemView.findViewById(R.id.timer);
            txtviewtotp = itemView.findViewById(R.id.txtviewtotp);
        }
    }
}