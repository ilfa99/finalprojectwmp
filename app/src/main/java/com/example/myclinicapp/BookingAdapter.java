package com.example.myclinicapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> doctor_id, name_id, date_id;
    private DatabaseHelper DB;

    public BookingAdapter(Context context, ArrayList<String> doctor_id, ArrayList<String> name_id, ArrayList<String> date_id) {
        this.context = context;
        this.doctor_id = doctor_id;
        this.name_id = name_id;
        this.date_id = date_id;
        this.DB = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String docName = String.valueOf(doctor_id.get(position));
        String patName = String.valueOf(name_id.get(position));
        String oldDate = String.valueOf(date_id.get(position));

        holder.tvDoctor.setText(docName);
        holder.tvName.setText("Patient: " + patName);
        holder.tvDate.setText(oldDate);

        // --- TOMBOL EDIT (PENSIL) ---
        holder.btnEdit.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String newDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

                        boolean checkUpdate = DB.updateBookingDate(patName, docName, oldDate, newDate);
                        if(checkUpdate) {
                            date_id.set(position, newDate);
                            notifyItemChanged(position);
                            Toast.makeText(context, "Rescheduled to: " + newDate, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to Reschedule", Toast.LENGTH_SHORT).show();
                        }
                    }, year, month, day);
            datePickerDialog.show();
        });

        // --- TOMBOL DELETE (SAMPAH) ---
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Cancel Appointment")
                    .setMessage("Are you sure want to cancel?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        boolean checkDelete = DB.deleteBooking(patName, docName, oldDate);
                        if(checkDelete) {
                            doctor_id.remove(position);
                            name_id.remove(position);
                            date_id.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, doctor_id.size());
                            Toast.makeText(context, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return name_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctor, tvName, tvDate;
        ImageView btnEdit, btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}