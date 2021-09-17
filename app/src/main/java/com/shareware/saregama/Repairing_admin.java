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
import com.shareware.saregama.Model.InquiryModel;
import com.shareware.saregama.Model.RepairInquiryModel;
import com.shareware.saregama.ViewHolder.MyInquiryAdapter;
import com.shareware.saregama.ViewHolder.MyRepairAdapter;
import com.shareware.saregama.common.Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Repairing_admin extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference inquiryViewList;


    String formattedDate;
    Calendar myCalendar;
    EditText dateEtd;
    Button findDateBtn, getPdfBtn;
    // FirebaseRecyclerAdapter<InquiryModel, InquiryViewHolder> adapter;

    TextView slipNumberTv, buyernameTv, contactnumberTv, modelTv, engineerTv, problemTv, dateTv, receivedItemTv, sellernameTv;

    EditText etdSlipNumber, etdbuyername, etdcontactnumber, etdmodel, etdEngineer, etdproblem, etddateTv, etdReceivedItem, etdsellername;

    private PdfPCell cell;


    private String path;
    private File dir;
    private File file;
    private Button getpdfAllbtn,fromToBtn;

    String username;
    ArrayList<RepairInquiryModel> dataofSeller = new ArrayList<RepairInquiryModel>();
    RepairInquiryModel dataSeller;
    //use to set background color
    BaseColor myColor1 = WebColors.getRGBColor("#E0E0E0");
    BaseColor myColor3 = WebColors.getRGBColor("#EEEEEE");
    BaseColor myColor2 = WebColors.getRGBColor("#BDBDBD");

    ImageView callimage;
    boolean flag=false;
    private String sendDate,startDate,endDate;
    private EditText etdStartingDate,etdEndingDate;
    private ProgressDialog pd;
    private ArrayList<RepairInquiryModel> inquiryList;
    private MyRepairAdapter Radapter;

    private ArrayList<String> inquiryListKey;

    private String formattedDate1,startdate1,enddate1;

    SimpleDateFormat simpleDateFormatMain = new SimpleDateFormat("yyyy-MM-dd");
    private String formattedDate2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairing_admin);
        database = FirebaseDatabase.getInstance();

        inquiryViewList = database.getReference("RepairInquiry");
        Intent intent = getIntent();
        username = intent.getStringExtra("id");
        // Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        inquiryListKey = new ArrayList<>();
        inquiryList = new ArrayList<>();
        recyclerView =(RecyclerView) findViewById(R.id.recycler_history);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(Repairing_admin.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RepairInquiryModel inquiryModel = inquiryList.get(position);
                showAddModelDialog(inquiryModel);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                final String key = inquiryListKey.get(position);
                final RepairInquiryModel inquiryModel = inquiryList.get(position);
                //  Toast.makeText(InquiryViewForm.this, "okay", Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(Repairing_admin.this,view);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu_seller);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                showUpdateDialog(key,inquiryModel);
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
        getpdfAllbtn = findViewById(R.id.btnpdfAll);
        getpdfAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataofSeller.isEmpty()) {
                    Toast.makeText(Repairing_admin.this, "Select particular date!!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (ContextCompat.checkSelfPermission(Repairing_admin.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Permission is not granted
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Repairing_admin.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                            Toast.makeText(Repairing_admin.this, "Grant the permission for Storage !!", Toast.LENGTH_SHORT).show();
                        } else {
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions(Repairing_admin.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    } else {
                        final ProgressDialog pd = new ProgressDialog(Repairing_admin.this);
                        pd.setMessage("Creating Pdf...");
                        pd.show();

                        // Permission has already been granted
                        // Toast.makeText(InquiryViewForm.this, "" + model.getMobileNo(), Toast.LENGTH_SHORT).show();
                        if(!flag) {
                            if (dateEtd.getText().toString().equals("")) {
                                sendDate = formattedDate1;
                                if(Common.ADMIN.equals(username)) {
                                    inquiryViewList.orderByChild("date").equalTo(sendDate).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataofSeller.clear();
                                            for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                                dataSeller = demo.getValue(RepairInquiryModel.class);
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
                                else {
                                    inquiryViewList.orderByChild("date").equalTo(sendDate).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataofSeller.clear();
                                            for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                                dataSeller = demo.getValue(RepairInquiryModel.class);
                                                if(dataSeller.getSellerName().equals(username))
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
                                if(Common.ADMIN.equals(username)) {
                                    inquiryViewList.orderByChild("date").equalTo(sendDate).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataofSeller.clear();
                                            for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                                dataSeller = demo.getValue(RepairInquiryModel.class);
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
                                else {
                                    inquiryViewList.orderByChild("date").equalTo(sendDate).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataofSeller.clear();
                                            for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                                dataSeller = demo.getValue(RepairInquiryModel.class);
                                                if(dataSeller.getSellerName().equals(username))
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
                        else {
                            if(Common.ADMIN.equals(username)) {
                                inquiryViewList.orderByChild("date").startAt(startdate1).endAt(enddate1).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataofSeller.clear();
                                        for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                            dataSeller = demo.getValue(RepairInquiryModel.class);
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
                            else {
                                inquiryViewList.orderByChild("date").startAt(startdate1).endAt(enddate1).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataofSeller.clear();
                                        for (DataSnapshot demo : dataSnapshot.getChildren()) {
                                            dataSeller = demo.getValue(RepairInquiryModel.class);
                                            if(dataSeller.getSellerName().equals(username))
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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Repairing_admin.this);
                alertDialog.setTitle("Find Details");
                alertDialog.setMessage("Enter From and To Date");
                LayoutInflater layoutInflater = Repairing_admin.this.getLayoutInflater();
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
                        new DatePickerDialog(Repairing_admin.this, date, myCalendar
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
                        new DatePickerDialog(Repairing_admin.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }

                });
                alertDialog.setView(add_menu_layout);
                alertDialog.setIcon(R.drawable.ic_contact_mail_black_24dp);

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
        dateEtd = (EditText) findViewById(R.id.dateEtd);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
        formattedDate1 = simpleDateFormatMain.format(c);
        formattedDate = df.format(c);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Saregama/RepairInquiryFormPDF";
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
                new DatePickerDialog(Repairing_admin.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        findDateBtn = (Button) findViewById(R.id.btnFind);
        findDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataofSeller.clear();
                String myFormat1 = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                formattedDate2 = sdf1.format(myCalendar.getTime());
                loadSellerViewList(formattedDate2);
            }
        });
        if (Common.isConnectedToInternet(getBaseContext())) {
            //   Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
            loadSellerList();

        } else {
            Toast.makeText(Repairing_admin.this, "Please check your connection", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void loadSellerListFromTo(String startdate, String enddate) {
        startDate = startdate;
        endDate = enddate;
        flag = true;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        inquiryViewList.orderByChild("date").startAt(startDate).endAt(endDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot inquiryData : dataSnapshot.getChildren()){

                    dataSeller = inquiryData.getValue(RepairInquiryModel.class);
                    if(Common.ADMIN.equals(username)){
                        inquiryListKey.add(inquiryData.getKey());
                        inquiryList.add(dataSeller);
                        dataofSeller.add(dataSeller);
                        //Toast.makeText(InquiryViewForm.this, String.valueOf(inquiryList.size()), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        if (dataSeller.getSellerName().equals(username)){
                            inquiryList.add(dataSeller);
                            inquiryListKey.add(inquiryData.getKey());
                            dataofSeller.add(dataSeller);
                        }
                    }
                }

                Toast.makeText(Repairing_admin.this, String.valueOf(inquiryList.size()), Toast.LENGTH_SHORT).show();
                Radapter = new MyRepairAdapter(Repairing_admin.this,inquiryList,inquiryListKey);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Repairing_admin.this);
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

        inquiryList.clear();
        inquiryListKey.clear();
    }



    private void deleteCategory(String key) {
        inquiryViewList.child(key).removeValue();
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

    private void showUpdateDialog(final String key, final RepairInquiryModel item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Repairing_admin.this);
        alertDialog.setTitle("Update Details");
        alertDialog.setMessage("Check Full information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.update_repair_inquiry_design_layout, null);
        etdbuyername = add_menu_layout.findViewById(R.id.etdbuyerName);
        etdcontactnumber = add_menu_layout.findViewById(R.id.etdcontactNumber);
        etdSlipNumber = add_menu_layout.findViewById(R.id.etdslipnumber);
        etdmodel = add_menu_layout.findViewById(R.id.etdmodel);
        etdEngineer = add_menu_layout.findViewById(R.id.etdengineer);
        etdproblem = add_menu_layout.findViewById(R.id.etdproblem);
        etdReceivedItem = add_menu_layout.findViewById(R.id.etdreceiveditem);
        etddateTv = add_menu_layout.findViewById(R.id.etddate);
        etdsellername = add_menu_layout.findViewById(R.id.etdsellerName);

        etdbuyername.setText(item.getBuyerName());
        etdcontactnumber.setText(item.getMobileNo());
        etdmodel.setText(item.getModelName());
        etdSlipNumber.setText(item.getSlipNumber());

        etddateTv.setText(item.getDate());
        etdEngineer.setText(item.getEngineer());
        etdsellername.setText(item.getSellerName());
        etdproblem.setText(item.getProblem());
        etdReceivedItem.setText(item.getReceiveitem());


        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_contact_mail_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setBuyerName(etdbuyername.getText().toString());
                item.setMobileNo(etdcontactnumber.getText().toString());
                item.setModelName(etdmodel.getText().toString());
                item.setSlipNumber(etdSlipNumber.getText().toString());
                item.setDate(etddateTv.getText().toString());
                item.setEngineer(etdEngineer.getText().toString());
                item.setSellerName(etdsellername.getText().toString());
                item.setProblem(etdproblem.getText().toString());
                item.setReceiveitem(etdReceivedItem.getText().toString());
                inquiryViewList.child(key).setValue(item);
                Toast.makeText(Repairing_admin.this, "Values Updated", Toast.LENGTH_SHORT).show();
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


        alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        alertDialog.show();


    }


    private void createPDFAll(ArrayList<RepairInquiryModel> dataofSeller) {
        Document doc = new Document();
        try {

            Log.e("PDFCreator", "PDF Path: " + path);
            if (!flag) {
                if (dateEtd.getText().toString().equals("")) {
                    file = new File(dir, "data of " + formattedDate + ".pdf");
                } else
                    file = new File(dir, "data of " + dateEtd.getText().toString() + ".pdf");
            }
            else {
                file = new File(dir,"data of "+etdStartingDate.getText().toString() + " to " +etdEndingDate.getText().toString() +".pdf" );

            }
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
                for (int i = 0; i < dataofSeller.size(); i++) {
                    dataSeller = dataofSeller.get(i);
                    //table.setHeaderRows(3);
                    cell = new PdfPCell(new Phrase(String.valueOf(i+1)));
                    cell.setRowspan(8);
                    table.addCell(cell);

                    table.addCell("Buyer Name ");
                    table.addCell(dataSeller.getBuyerName());

                    table.addCell("Contact Number ");
                    table.addCell(dataSeller.getMobileNo());


                    table.addCell("Slip Number ");
                    table.addCell(dataSeller.getSlipNumber());


                    table.addCell("Model ");
                    table.addCell(dataSeller.getModelName());


                    table.addCell("Engineer ");
                    table.addCell(dataSeller.getEngineer());


                    table.addCell("Problem ");
                    table.addCell(dataSeller.getProblem());

                    table.addCell("Received Item ");
                    table.addCell(dataSeller.getReceiveitem());

                    table.addCell("Seller Name ");
                    table.addCell(dataSeller.getSellerName());


                }
                doc.add(table);
                dataofSeller.clear();
                Toast.makeText(getApplicationContext(), "PDF created", Toast.LENGTH_LONG).show();

            } catch (DocumentException de) {
                Toast.makeText(this, "hey1" + de, Toast.LENGTH_SHORT).show();
                Log.e("PDFCreator", "DocumentException:" + de);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void loadSellerViewList(String s) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        inquiryViewList.orderByChild("date").equalTo(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot inquiryData : dataSnapshot.getChildren()){

                    dataSeller = inquiryData.getValue(RepairInquiryModel.class);
                    if(Common.ADMIN.equals(username)){
                        inquiryListKey.add(inquiryData.getKey());
                        inquiryList.add(dataSeller);
                        dataofSeller.add(dataSeller);
                    }
                    else {
                        if (dataSeller.getSellerName().equals(username)){
                            inquiryList.add(dataSeller);
                            inquiryListKey.add(inquiryData.getKey());
                            dataofSeller.add(dataSeller);
                        }
                    }
                }
                Toast.makeText(Repairing_admin.this, String.valueOf(inquiryList.size()), Toast.LENGTH_SHORT).show();
                Radapter = new MyRepairAdapter(Repairing_admin.this,inquiryList,inquiryListKey);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Repairing_admin.this);
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


        inquiryList.clear();
        inquiryListKey.clear();
    }

    private void showAddModelDialog(final RepairInquiryModel item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Repairing_admin.this);
        alertDialog.setTitle("View Details");
        alertDialog.setMessage("Full information of Inquiry");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.view_repair_inquiry_design_layout, null);

        buyernameTv = add_menu_layout.findViewById(R.id.TvbuyerName);
        contactnumberTv = add_menu_layout.findViewById(R.id.TvcontactNumber);
        modelTv = add_menu_layout.findViewById(R.id.Tvmodel);
        slipNumberTv = add_menu_layout.findViewById(R.id.Tvslipnumber);
        dateTv = add_menu_layout.findViewById(R.id.Tvdate);
        problemTv = add_menu_layout.findViewById(R.id.Tvproblem);
        sellernameTv = add_menu_layout.findViewById(R.id.TvsellerName);
        engineerTv =add_menu_layout.findViewById(R.id.Tvengineer);
        receivedItemTv = add_menu_layout.findViewById(R.id.TvreceivedItem);
        getPdfBtn = add_menu_layout.findViewById(R.id.btnGetPdf);
        callimage = add_menu_layout.findViewById(R.id.callbtn);
        buyernameTv.setText("Name : " + item.getBuyerName());
        contactnumberTv.setText("Contact Number : " + item.getMobileNo());
        modelTv.setText("Item : " + item.getModelName());
        engineerTv.setText("Engineer : " + item.getEngineer());
        dateTv.setText("Issue Date : " + item.getDate());
        slipNumberTv.setText("Slip No : " + item.getSlipNumber());
        sellernameTv.setText("Seller Name : " + item.getSellerName());
        problemTv.setText("Problem : "+item.getProblem());
        receivedItemTv.setText("Received Item :" + item.getReceiveitem());
        getPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //            createandDisplayPdf("Hello world");
                //Toast.makeText(ViewSellerForm.this, "Thank You", Toast.LENGTH_SHORT).show();
                try {
                    createPDF(item);
                } catch (FileNotFoundException e) {
                    Toast.makeText(Repairing_admin.this, "hey " + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (DocumentException e) {
                    Toast.makeText(Repairing_admin.this, "bye " + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        callimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Repairing_admin.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale( Repairing_admin.this,
                            Manifest.permission.CALL_PHONE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Toast.makeText(Repairing_admin.this, "Grant the permission for Call !!", Toast.LENGTH_SHORT).show();
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions( Repairing_admin.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.

                    }
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + item.getMobileNo()));
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

    public void createPDF(RepairInquiryModel item) throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        try {

            Log.e("PDFCreator", "PDF Path: " + path);
            file = new File(dir, item.getBuyerName() + ".pdf");
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

                cell = new PdfPCell(new Phrase("Date " + item.getDate()));
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
                table.addCell("Slip Number ");
                table.addCell(item.getSlipNumber());

                table.addCell(String.valueOf(4));
                table.addCell("Model ");
                table.addCell( item.getModelName());

                table.addCell(String.valueOf(5));
                table.addCell("Engineer ");
                table.addCell(item.getEngineer());


                table.addCell(String.valueOf(6));
                table.addCell("Problem ");
                table.addCell(item.getProblem());

                table.addCell(String.valueOf(7));
                table.addCell("Received Item ");
                table.addCell(item.getReceiveitem());

                table.addCell(String.valueOf(7));
                table.addCell("Seller Name ");
                table.addCell(item.getSellerName());

                doc.add(table);
                dataofSeller.clear();
                Toast.makeText(getApplicationContext(), "create " + item.getBuyerName() + ".PDF", Toast.LENGTH_LONG).show();
            } catch (DocumentException de) {
                Toast.makeText(this, "hey1" + de, Toast.LENGTH_SHORT).show();
                Log.e("PDFCreator", "DocumentException:" + de);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEtd.setText(sdf.format(myCalendar.getTime()));
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        formattedDate1 = sdf1.format(myCalendar.getTime());

    }
    private void updateLabelStaring() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etdStartingDate.setText(sdf.format(myCalendar.getTime()));
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        startdate1 = sdf1.format(myCalendar.getTime());
    }
    private void updateLabelEnding() {

        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etdEndingDate.setText(sdf.format(myCalendar.getTime()));
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        enddate1 = sdf1.format(myCalendar.getTime());
    }

    private void loadSellerList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        inquiryViewList.orderByChild("date").equalTo(formattedDate1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot inquiryData : dataSnapshot.getChildren()){

                    dataSeller = inquiryData.getValue(RepairInquiryModel.class);
                    if(Common.ADMIN.equals(username)){
                        inquiryListKey.add(inquiryData.getKey());
                        inquiryList.add(dataSeller);
                        dataofSeller.add(dataSeller);
                        //Toast.makeText(InquiryViewForm.this, String.valueOf(inquiryList.size()), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        if (dataSeller.getSellerName().equals(username)){
                            inquiryList.add(dataSeller);
                            inquiryListKey.add(inquiryData.getKey());
                            dataofSeller.add(dataSeller);
                        }
                    }
                }

                Toast.makeText(Repairing_admin.this, String.valueOf(inquiryList.size()), Toast.LENGTH_SHORT).show();
                Radapter = new MyRepairAdapter(Repairing_admin.this,inquiryList,inquiryListKey);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Repairing_admin.this);
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

        inquiryList.clear();
        inquiryListKey.clear();
    }
}