package com.example.myclinicapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> doctor_id, name_id, date_id, time_id;
    private DatabaseHelper DB;

    // Constructor diperbarui menerima time_id
    public BookingAdapter(Context context, ArrayList<String> doctor_id, ArrayList<String> name_id, ArrayList<String> date_id, ArrayList<String> time_id) {
        this.context = context;
        this.doctor_id = doctor_id;
        this.name_id = name_id;
        this.date_id = date_id;
        this.time_id = time_id;
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
        String oldTime = String.valueOf(time_id.get(position));

        holder.tvDoctor.setText(docName);
        holder.tvName.setText("Patient: " + patName);

        // --- PERBAIKAN FORMAT TEXT DI SINI ---
        // Menggunakan simbol titik tebal (•) agar lebih rapi
        holder.tvDate.setText(oldDate + "  •  " + oldTime);

        // ... kode tombol edit & delete biarkan sama ...
        // --- TOMBOL EDIT (UPDATE TANGGAL & JAM) ---
        holder.btnEdit.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // 1. Buka Date Picker Dulu
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String newDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

                        // 2. Setelah pilih tanggal, Buka Time Picker
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                (view1, hourOfDay, minute1) -> {
                                    // Format jam biar ada AM/PM
                                    String amPm = (hourOfDay >= 12) ? "PM" : "AM";
                                    int hour12 = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                                    if (hour12 == 0) hour12 = 12;
                                    String newTime = String.format("%02d:%02d %s", hour12, minute1, amPm);

                                    // 3. Update Database dengan Fungsi BARU
                                    boolean checkUpdate = DB.updateBookingDateTime(patName, docName, oldDate, newDate, newTime);

                                    if(checkUpdate) {
                                        // Update tampilan list secara langsung
                                        date_id.set(position, newDate);
                                        time_id.set(position, newTime);
                                        notifyItemChanged(position);
                                        Toast.makeText(context, "Rescheduled to: " + newDate + " " + newTime, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to Reschedule", Toast.LENGTH_SHORT).show();
                                    }
                                }, hour, minute, false);
                        timePickerDialog.show();

                    }, year, month, day);
            datePickerDialog.show();
        });

        // --- TOMBOL DELETE ---
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
                            time_id.remove(position);
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