package com.example.zhu.test4;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.soundcloud.android.crop.Crop;
import java.io.File;


public class Detail extends AppCompatActivity {
    final private int RESULT_CODE = -1;
    private Item item;
    private ImageView myImage;
    private EditText myEditText;
    private TextView myTextView;
    private CalendarView myCalendarView;
    private String dates;
    private Bitmap bm;

    private Uri myUri;

    public static final int ASPECT_X = 24;
    public static final int ASPECT_Y = 29;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myTextView =  (TextView) findViewById(R.id.dateText);
        myCalendarView = (CalendarView)findViewById(R.id.calendarView);
        myEditText = (EditText)findViewById(R.id.edit_name);



       getWindow().setEnterTransition(TransitionInflater.from(this)
                .inflateTransition(R.transition.slide));
       getWindow().setReturnTransition(TransitionInflater.from(this)
                .inflateTransition(R.transition.slide));
       getWindow().setSharedElementEnterTransition(initSharedElementEnterTransition().setDuration(600));
       getWindow().setSharedElementExitTransition(new Slide());
       chooseCalender();


        Intent intent = getIntent();
//        position = intent.getIntExtra("position",0);
        item =(Item)intent.getSerializableExtra("myItem");
        myImage = (ImageView)findViewById(R.id.myImage);

        if(item.getUri()!=null) myImage.setImageURI(Uri.parse(item.getUri()));

    }

    private void chooseCalender() {
        myCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                dates = ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
                myTextView.setText(dates);
            }
        });
    }

    private Transition initSharedElementEnterTransition() {
        final Transition sharedTransition=TransitionInflater.from(this).inflateTransition(R.transition.change_bounds);
        sharedTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                sharedTransition.removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        return sharedTransition;
    }

    @Override
    public boolean onSupportNavigateUp() {

        Intent intent = new Intent();
        intent.putExtra("date",dates);
       if(myUri!=null) intent.putExtra("uri",myUri.toString());
        intent.putExtra("name",myEditText.getText().toString());
        setResult(RESULT_CODE,intent);

//        setResult(RESULT_CODE,intent);
        onBackPressed();
//        finish();

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_memu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.detail_action_select) {
            myImage.setImageDrawable(null);
            Crop.pickImage(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
//        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"+ String.valueOf(System.currentTimeMillis())));
        Crop.of(source, destination).withAspect(ASPECT_X, ASPECT_Y).start(this);
//        Crop.of(source, destination).withAspect(ASPECT_X, ASPECT_Y).start(getActivity(), RegistrationDialog.this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            myUri = Crop.getOutput(result);
            Log.d("itemcount=",myUri.toString());
            myImage.setImageURI(myUri);
//            item.setUri(myUri.toString());


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
