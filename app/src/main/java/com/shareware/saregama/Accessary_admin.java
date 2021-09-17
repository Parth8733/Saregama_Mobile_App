package com.shareware.saregama;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shareware.saregama.Interface.RecyclerItemClickListener;
import com.shareware.saregama.Model.AccessoriesInquiryModel;
import com.shareware.saregama.Model.SellerModel;
import com.shareware.saregama.ViewHolder.MyAccessaryAdapter;
import com.shareware.saregama.ViewHolder.MySellerAdapter;
import com.shareware.saregama.common.Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Accessary_admin extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference sellerViewList;

    String formattedDate;
    Calendar myCalendar;
    EditText dateEtd;
    Button findDateBtn,getPdfBtn;

    TextView buyerName,contactnumber,requirement,sellerName,dateTV;

    EditText etdbuyerName,etdcontactnumber,etdrequirement,etdsellerName,etdDATE;

    private PdfPCell cell;


    private String path;
    private File dir;
    private File file;
    private Button getpdfAllbtn;

    ArrayList<AccessoriesInquiryModel> dataofSeller = new ArrayList<>();
    AccessoriesInquiryModel dataSeller;

    BaseColor myColor1 = WebColors.getRGBColor("#E0E0E0");
    BaseColor myColor3 = WebColors.getRGBColor("#EEEEEE");
    BaseColor myColor2 = WebColors.getRGBColor("#BDBDBD");
    private String username;
    private String sendDate;
    private boolean flag =false;
    private String startDate,endDate;
    private Button fromToBtn;
    private EditText etdStartingDate,etdEndingDate;

    private ArrayList<AccessoriesInquiryModel> accessoriesInquiryList;
    private ArrayList<String> accessoriesKey;

    private MyAccessaryAdapter Radapter;
    private ImageView callimage;


    private String formattedDate1,startdate1,enddate1;

    SimpleDateFormat simpleDateFormatMain = new SimpleDateFormat("yyyy-MM-dd");
    private String formattedDate2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessary_admin);
        Intent intent = getIntent();
        username = intent.getStringExtra("id");
        database = FirebaseDatabase.getInstance();
        sellerViewList = database.getReference("AccessoriesInquiry");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_history);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        accessoriesInquiryList = new ArrayList<>();
        accessoriesKey = new ArrayList<>();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(Accessary_admin.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AccessoriesInquiryModel sellerModel = accessoriesInquiryList.get(position);
                showAddModelDialog(sellerModel);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                final String key = accessoriesKey.get(position);
                final AccessoriesInquiryModel sellerModel = accessoriesInquiryList.get(position);
                //  Toast.makeText(InquiryViewForm.this, "okay", Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(Accessary_admin.this,view);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu_seller);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                showUpdateDialog(key,sellerModel);
                                //handle menu1 click
                                return true;
                            case R.id.menu2:
                                //handle menu2 click
                                deleteCategory(key);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        }));
        getpdfAllbtn =(Button)findViewById(R.id.btnpdfAll);
        getpdfAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataofSeller.isEmpty()) {
                    Toast.makeText(Accessary_admin.this, "Select particular date!!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (ContextCompat.checkSelfPermission(Accessary_admin.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Permission is not granted
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Accessary_admin.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                            Toast.makeText(Accessary_admin.this, "Grant the permission for Storage !!", Toast.LENGTH_SHORT).show();
                        } else {
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions(Accessary_admin.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    } else {
                        final ProgressDialog pd = new ProgressDialog(Accessary_admin.this);
                        pd.setMessage("Creating Pdf...");
                        pd.show();
                        // Permission has already been granted
                        // Toast.makeText(InquiryViewForm.this, "" + model.getMobileNo(), Toast.LENGTH_SHORT).show();
                        if (!flag) {
                            if (dateEtd.getText().toString().equals("")) {
                                sendDate = formattedDate1;
                                if (Common.ADMIN.equals(username)) {
                                    sellerViewList.orderByChild("billdate").equalTo(sendDate).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataofSeller.clear();
                                            for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                                dataSeller = demo.getValue(AccessoriesInquiryModel.class);
                                                dataofSeller.add(dataSeller);
                                            }
                                            createPDFAll(dataofSeller);
                                            pd.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    sellerViewList.orderByChild("billdate").equalTo(sendDate).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataofSeller.clear();
                                            for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                                dataSeller = demo.getValue(AccessoriesInquiryModel.class);
                                                if (dataSeller.getSellerName().equals(username))
                                                    dataofSeller.add(dataSeller);

                                            }
                                            createPDFAll(dataofSeller);
                                            pd.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            } else {
                                String myFormat1 = "yyyy-MM-dd"; //In which you need put here
                                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                                sendDate = sdf1.format(myCalendar.getTime());
                                if (Common.ADMIN.equals(username)) {
                                    sellerViewList.orderByChild("billdate").equalTo(sendDate).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataofSeller.clear();
                                            for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                                dataSeller = demo.getValue(AccessoriesInquiryModel.class);
                                                dataofSeller.add(dataSeller);
                                            }
                                            createPDFAll(dataofSeller);
                                            pd.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    sellerViewList.orderByChild("billdate").equalTo(sendDate).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataofSeller.clear();
                                            for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                                dataSeller = demo.getValue(AccessoriesInquiryModel.class);
                                                if (dataSeller.getSellerName().equals(username))
                                                    dataofSeller.add(dataSeller);

                                            }
                                            createPDFAll(dataofSeller);
                                            pd.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        } else {
                            if (Common.ADMIN.equals(username)) {
                                sellerViewList.orderByChild("billdate").startAt(startDate).endAt(endDate).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataofSeller.clear();
                                        for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                            dataSeller = demo.getValue(AccessoriesInquiryModel.class);
                                            dataofSeller.add(dataSeller);
                                        }
                                        createPDFAll(dataofSeller);
                                        pd.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                sellerViewList.orderByChild("billdate").startAt(startDate).endAt(endDate).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataofSeller.clear();
                                        for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                            dataSeller = demo.getValue(AccessoriesInquiryModel.class);
                                            if (dataSeller.getSellerName().equals(username))
                                                dataofSeller.add(dataSeller);

                                        }
                                        createPDFAll(dataofSeller);
                                        pd.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    }



                }
            }

        });
        fromToBtn =(Button)findViewById(R.id.btnFromTo);
        fromToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Accessary_admin.this);
                alertDialog.setTitle("Find Details");
                alertDialog.setMessage("Enter From and To Date");
                LayoutInflater layoutInflater = Accessary_admin.this.getLayoutInflater();
                View add_menu_layout = layoutInflater.inflate(R.layout.from_to_date_design_layout, null);
                etdStartingDate = add_menu_layout.findViewById(R.id.etdStartindDate);
                etdEndingDate = add_menu_layout.findViewById(R.id.etdEndingDate);

                etdStartingDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                updateLabelStaring();
                            }

                        };
                        new DatePickerDialog(Accessary_admin.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                etdEndingDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                updateLabelEnding();
                            }

                        };
                        new DatePickerDialog(Accessary_admin.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }

                });
                alertDialog.setView(add_menu_layout);
                alertDialog.setIcon(R.drawable.ic_menu_slideshow);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        loadSellerListFromTo(startdate1,enddate1);
                        // Toast.makeText(InquiryViewForm.this, etdStartingDate.getText().toString()+" "+etdEndingDate.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        myCalendar = Calendar.getInstance();
        dateEtd = (EditText)findViewById(R.id.dateEtd);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
        formattedDate1 = simpleDateFormatMain.format(c);
        formattedDate = df.format(c);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Saregama/AccessoriesFormPDF";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateEtd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(Accessary_admin.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        findDateBtn= (Button)findViewById(R.id.btnFind);
        findDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //       Toast.makeText(ViewSellerForm.this, dateEtd.getText().toString(), Toast.LENGTH_SHORT).show();
                dataofSeller.clear();
                String myFormat1 = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                formattedDate2 = sdf1.format(myCalendar.getTime());
                loadSellerViewList(formattedDate2);
            }
        });
        if (Common.isConnectedToInternet(getBaseContext())) {
            // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
            loadSellerList();
        } else {
            Toast.makeText(Accessary_admin.this, "Please check your connection", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void updateLabelEnding() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etdEndingDate.setText(sdf.format(myCalendar.getTime()));
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        enddate1 = sdf1.format(myCalendar.getTime());
    }

    private void loadSellerListFromTo(String toString, String toString1) {
        startDate = toString;
        endDate = toString1;
        flag = true;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sellerViewList.orderByChild("billdate").startAt(startDate).endAt(endDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sellerData : dataSnapshot.getChildren()){

                    dataSeller = sellerData.getValue(AccessoriesInquiryModel.class);
                    if(Common.ADMIN.equals(username)){
                        accessoriesKey.add(sellerData.getKey());
                        accessoriesInquiryList.add(dataSeller);
                        dataofSeller.add(dataSeller);
                        //Toast.makeText(InquiryViewForm.this, String.valueOf(inquiryList.size()), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        if (dataSeller.getSellerName().equals(username)){
                            accessoriesInquiryList.add(dataSeller);
                            accessoriesKey.add(sellerData.getKey());
                            dataofSeller.add(dataSeller);
                        }
                    }
                }
                Toast.makeText(Accessary_admin.this, String.valueOf(accessoriesInquiryList.size()), Toast.LENGTH_SHORT).show();
                Radapter = new MyAccessaryAdapter(Accessary_admin.this,accessoriesInquiryList,accessoriesKey);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Accessary_admin.this);
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(Radapter);
                recyclerView.setHasFixedSize(true);
                Radapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        accessoriesInquiryList.clear();
        accessoriesKey.clear();
    }

    private void loadSellerViewList(String toString) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sellerViewList.orderByChild("billdate").equalTo(toString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sellerData : dataSnapshot.getChildren()){

                    dataSeller = sellerData.getValue(AccessoriesInquiryModel.class);
                    if(Common.ADMIN.equals(username)){
                        accessoriesKey.add(sellerData.getKey());
                        accessoriesInquiryList.add(dataSeller);
                        dataofSeller.add(dataSeller);
                        //Toast.makeText(InquiryViewForm.this, String.valueOf(inquiryList.size()), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        if (dataSeller.getSellerName().equals(username)){
                            accessoriesInquiryList.add(dataSeller);
                            accessoriesKey.add(sellerData.getKey());
                            dataofSeller.add(dataSeller);
                        }
                    }
                }
                Toast.makeText(Accessary_admin.this, String.valueOf(accessoriesInquiryList.size()), Toast.LENGTH_SHORT).show();
                Radapter = new MyAccessaryAdapter(Accessary_admin.this,accessoriesInquiryList,accessoriesKey);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Accessary_admin.this);
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(Radapter);
                recyclerView.setHasFixedSize(true);
                Radapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        accessoriesInquiryList.clear();
        accessoriesKey.clear();
    }



    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEtd.setText(sdf.format(myCalendar.getTime()));
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        formattedDate1 = sdf1.format(myCalendar.getTime());
    }

    private void loadSellerList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sellerViewList.orderByChild("billdate").equalTo(formattedDate1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sellerData : dataSnapshot.getChildren()){

                    dataSeller = sellerData.getValue(AccessoriesInquiryModel.class);
                    if(Common.ADMIN.equals(username)){
                        accessoriesKey.add(sellerData.getKey());
                        accessoriesInquiryList.add(dataSeller);
                        dataofSeller.add(dataSeller);
                    //    Toast.makeText(Accessary_admin.this, String.valueOf(accessoriesInquiryList.size()), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        if (dataSeller.getSellerName().equals(username)){
                            accessoriesInquiryList.add(dataSeller);
                            accessoriesKey.add(sellerData.getKey());
                            dataofSeller.add(dataSeller);
                        }
                    }
                }
                Toast.makeText(Accessary_admin.this, String.valueOf(accessoriesInquiryList.size()), Toast.LENGTH_SHORT).show();
                Radapter = new MyAccessaryAdapter(Accessary_admin.this,accessoriesInquiryList,accessoriesKey);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Accessary_admin.this);
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(Radapter);
                recyclerView.setHasFixedSize(true);
                Radapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        accessoriesInquiryList.clear();
        accessoriesKey.clear();
    }


    private void updateLabelStaring() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etdStartingDate.setText(sdf.format(myCalendar.getTime()));
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        startdate1 = sdf1.format(myCalendar.getTime());
    }

    private void createPDFAll(ArrayList<AccessoriesInquiryModel> dataofSeller) {
        Document doc = new Document();
        try {

            Log.e("PDFCreator", "PDF Path: "+path );
            if (!flag) {
                if (dateEtd.getText().toString().equals("")) {
                    file = new File(dir, "data of " + formattedDate + ".pdf");
                } else
                    file = new File(dir, "data of " + dateEtd.getText().toString() + ".pdf");
            }
            else {
                file = new File(dir, "data of " + etdStartingDate.getText().toString() + " to " + etdEndingDate.getText().toString() + ".pdf");
            }
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the documentf
            doc.open();
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.getHorizontalAlignment();
                cell.addElement(new Paragraph(" "));

                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                PdfPTable table = new PdfPTable(3);

                float[] columnWidth = new float[]{6, 30, 30};
                table.setWidths(columnWidth);


                cell = new PdfPCell(new Phrase("Saregama Mobile Shop"));
                cell.setBackgroundColor(myColor2);
                cell.setPadding(10f);
                cell.setColspan(2);
                table.addCell(cell);
                if (!flag) {
                    if (dateEtd.getText().toString().equals("")) {
                        cell = new PdfPCell(new Phrase("Date " + formattedDate));
                    } else
                        cell = new PdfPCell(new Phrase("Date " + dateEtd.getText().toString()));
                }
                else {
                    cell = new PdfPCell(new Phrase("Date " + etdStartingDate.getText().toString() +" To "+etdEndingDate.getText().toString()));
                    flag = false;
                }

                cell.setBackgroundColor(myColor2);
                cell.setPadding(10f);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(3);
                cell.setBackgroundColor(myColor1);
                cell = new PdfPCell(new Phrase("Sr.No"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Fields"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Field Details"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                for (int i=0;i<dataofSeller.size();i++) {
                    dataSeller =dataofSeller.get(i);
                    //table.setHeaderRows(3);
                    cell = new PdfPCell( new Phrase(String.valueOf(i+1)));
                    cell.setRowspan(4);
                    table.addCell(cell);

                    table.addCell("Buyer Name ");
                    table.addCell(dataSeller.getBuyerName());

                    table.addCell("Contact Number ");
                    table.addCell(dataSeller.getMobileNo());

                    table.addCell("Requirement ");
                    table.addCell(dataSeller.getRequirement());

                    table.addCell("Seller Name ");
                    table.addCell(dataSeller.getSellerName());



                }
                doc.add(table);
                dataofSeller.clear();
                Toast.makeText(getApplicationContext(), "PDF created", Toast.LENGTH_LONG).show();
            } catch (DocumentException de) {
                Toast.makeText(this, "hey1"+de, Toast.LENGTH_SHORT).show();
                Log.e("PDFCreator", "DocumentException:" + de);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void deleteCategory(String key) {
        sellerViewList.child(key).removeValue();
        Toast.makeText(this, "Form Deleted!!!", Toast.LENGTH_SHORT).show();
        if (!flag) {
            if (dateEtd.getText().toString().isEmpty())
                loadSellerViewList(formattedDate2);
            else
                loadSellerViewList(formattedDate2);
        }
        else
            loadSellerListFromTo(startdate1,enddate1);

    }

    private void showUpdateDialog(final String key, final AccessoriesInquiryModel sellerModel) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Accessary_admin.this);
        alertDialog.setTitle("Update Details");
        alertDialog.setMessage("Check Full information of Accessories Form");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.update_accessories_design_layout, null);
        etdbuyerName = add_menu_layout.findViewById(R.id.etdbuyerName);
        etdrequirement = add_menu_layout.findViewById(R.id.etdRequirement);
        etdcontactnumber = add_menu_layout.findViewById(R.id.etdcontactNumber);
        etdsellerName = add_menu_layout.findViewById(R.id.etdsellerName);
        etdDATE = add_menu_layout.findViewById(R.id.etddate);

        etdbuyerName.setText(sellerModel.getBuyerName());
        etdrequirement.setText(sellerModel.getRequirement());
        etdDATE.setText(sellerModel.getBilldate());
        etdcontactnumber.setText(sellerModel.getMobileNo());
        etdsellerName.setText(sellerModel.getSellerName());

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_contact_mail_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                    sellerModel.setBuyerName(etdbuyerName.getText().toString());
                    sellerModel.setMobileNo(etdcontactnumber.getText().toString());
                    sellerModel.setRequirement(etdrequirement.getText().toString());
                    sellerModel.setBilldate(etdDATE.getText().toString());
                    sellerModel.setSellerName(etdsellerName.getText().toString());
                sellerViewList.child(key).setValue(sellerModel);
                Toast.makeText(Accessary_admin.this, "Values Updated", Toast.LENGTH_SHORT).show();
                if (!flag) {
                    if (dateEtd.getText().toString().isEmpty())
                        loadSellerViewList(formattedDate2);
                    else
                        loadSellerViewList(formattedDate2);
                }
                else
                    loadSellerListFromTo(startdate1,enddate1);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showAddModelDialog(final AccessoriesInquiryModel sellerModel) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Accessary_admin.this);
        alertDialog.setTitle("View Details");
        alertDialog.setMessage("Full information of Client");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.view_accessories_design_layout, null);

        buyerName = add_menu_layout.findViewById(R.id.TvbuyerName);
        contactnumber = add_menu_layout.findViewById(R.id.TvcontactNumber);
        requirement = add_menu_layout.findViewById(R.id.Tvrequirement);
        dateTV = add_menu_layout.findViewById(R.id.Tvdate);
        sellerName = add_menu_layout.findViewById(R.id.TvsellerName);
        getPdfBtn =add_menu_layout.findViewById(R.id.btnGetPdf);
        callimage = add_menu_layout.findViewById(R.id.callbtn);
        buyerName.setText("Buyer Name "+sellerModel.getBuyerName());
        contactnumber.setText("Contact Number "+sellerModel.getMobileNo());
        requirement.setText("Requirement "+sellerModel.getRequirement());
        dateTV.setText("Date "+sellerModel.getBilldate());
        sellerName.setText("Seller Name "+sellerModel.getSellerName());
        getPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPDF(sellerModel);
                } catch (FileNotFoundException e) {
                    Toast.makeText(Accessary_admin.this, "hey "+e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (DocumentException e) {
                    Toast.makeText(Accessary_admin.this, "bye "+e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        callimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Accessary_admin.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale( Accessary_admin.this,
                            Manifest.permission.CALL_PHONE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Toast.makeText(Accessary_admin.this, "Grant the permission for Call !!", Toast.LENGTH_SHORT).show();
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions( Accessary_admin.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.

                    }
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + sellerModel.getMobileNo()));
                    startActivity(intent);
                    // Toast.makeText(InquiryViewForm.this, "" + model.getMobileNo(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_contact_mail_black_24dp);


        alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        alertDialog.show();

    }
    public void createPDF(AccessoriesInquiryModel item) throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        try {

            Log.e("PDFCreator", "PDF Path: "+path );
            file = new File(dir,  item.getBuyerName()+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.getHorizontalAlignment();
                cell.addElement(new Paragraph(" "));

                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                PdfPTable table = new PdfPTable(3);

                float[] columnWidth = new float[]{6, 30, 30};
                table.setWidths(columnWidth);


                cell = new PdfPCell();


                cell.setBackgroundColor(BaseColor.ORANGE);
                cell.setColspan(3);
                cell.addElement(pTable);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Saregama Mobile Shop"));
                cell.setBackgroundColor(myColor2);
                cell.setPadding(10f);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Date "+item.getBilldate()));
                cell.setBackgroundColor(myColor2);
                cell.setPadding(10f);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(3);
                cell.setBackgroundColor(myColor1);
                cell = new PdfPCell(new Phrase("Sr.No"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Fields"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Field Details"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(3);
                cell.setBorderColor(myColor3);
                table.addCell(String.valueOf(1));
                table.addCell("Buyer Name ");
                table.addCell(item.getBuyerName());


                table.addCell(String.valueOf(2));
                table.addCell("Contact Number ");
                table.addCell(item.getMobileNo());

                table.addCell(String.valueOf(3));
                table.addCell("Requirement ");
                table.addCell(item.getRequirement());


                table.addCell(String.valueOf(4));
                table.addCell("Seller Name ");
                table.addCell(item.getSellerName());


                doc.add(table);
                Toast.makeText(getApplicationContext(), "create " +item.getBuyerName() +".PDF", Toast.LENGTH_LONG).show();
                dataofSeller.clear();
            } catch (DocumentException de) {
                Toast.makeText(this, "hey1"+de, Toast.LENGTH_SHORT).show();
                Log.e("PDFCreator", "DocumentException:" + de);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
