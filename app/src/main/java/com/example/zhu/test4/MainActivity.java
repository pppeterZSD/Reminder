package com.example.zhu.test4;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeImageTransform;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.transition.Transition;
import android.view.View;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;
import java.util.Collections;
import java.util.List;

/**
 * My first android app:This is an birthday reminder app(or you may mark some important event)
 * The most fascinating part of this app is the UI design
 * I hope it could bring you a quite enjoyable experience
 * @author zhusd
 * @version 1.0
 */

public class MainActivity extends BaseSceneActivity  implements
        DiscreteScrollView.OnItemChangedListener<MyAdapter.ViewHolder>
        ,DiscreteScrollView.ScrollStateChangeListener<MyAdapter.ViewHolder>
        ,View.OnClickListener {

    public static final int REQUEST_DETAIL_NEW_ITEM=11102;
    final private int REQUEST_DETAIL = 111;
    private List<Item> data = Collections.emptyList();
    private TextView text;
    private ImageView image;
    private View convertView;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private InfiniteScrollAdapter infiniteAdapter;
    private DiscreteScrollView itemPicker;
    private String result_date;
    private String result_name;
    private String result_uri;
    private Shop shop;
    private TextView currentItemName;
    private TextView currentItemDate;
    private ImageView currentImage;
    private ImageView rateItemButton;
    private int dataSize;
    private int positionTransfer;
    private Item item;
    private Item newItem;
    int primarySize;
//    private AlarmManager aManager;
    AnimationDrawable anim;
    private GradientView gradientView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * in order to have a UI friendly interface, you'd better use a high version
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getSupportActionBar().hide();


        setContentView(R.layout.activity_main);


        /**
         *  to realize gradient transition while sliding the Card
         * @param gradientView  this view is for the purpose of achieving gradient color transition
         * @param currentItemName card name shown in the main page
         * @param currentItemDate card date shown in the main page
         * @param rateItemButton    this is used to record the alarm status
         *
         */

        gradientView = (GradientView)findViewById(R.id.gradient_view);
        currentItemName = (TextView) findViewById(R.id.item_name);
        currentItemDate = (TextView) findViewById(R.id.item_date);
        rateItemButton = (ImageView) findViewById(R.id.item_btn_rate);
        findViewById(R.id.item_btn_rate).setOnClickListener(this);

        /**
         * following views could be used in activity_main.xml
         * while I have set the visibility as  gone
         * because I have moved these functions to my navigation button
         * but you could use your custom navigation window, so you may need it
         * findViewById(R.id.item_btn_buy).setOnClickListener(this);
         * findViewById(R.id.item_btn_newItem).setOnClickListener(this);
         * findViewById(R.id.item_btn_add).setOnClickListener(this);
         * findViewById(R.id.item_btn_remove).setOnClickListener(this);
         */


        //to initialize activity transition  and sharing element
        initTransition();

        //initialize data
        shop = Shop.get();
        data=shop.getData();
        onItemChanged(data.get(0));
        gradientView.setForecast(1);

        //initialize infinite recycle view
        initRecycleView();

        //set item click listener in adapter
        initAdapterListener();

        //initialize menu of the navigation button
        initNavigationMenu();



    }



    private void initRecycleView() {
        /**
         * initialize infinite recycleview
         * @see String
         * @param infiniteAdapter a wrapper adapter of the original adapter
         * @param adapter context object
         * @param itemPicker   itemPicker of the image
         * Actually I use a library to make my recycle view infinite for convenience
         * but you could create your own infinite recycle view
         * If you don't need an infinite recycle view , just use the 'recycleView' as following
         * rather than the item_picker
         *
         *recyclerView = (RecyclerView) findViewById(R.id.item_picker);
         *recyclerView.setAdapter(adapter);
         */

        adapter = new MyAdapter(data);
        itemPicker = (DiscreteScrollView) findViewById(R.id.item_picker);
        //itemPicker.setOrientation(DSVOrientation.VERTICAL);
        infiniteAdapter = infiniteAdapter.wrap(adapter);
        itemPicker.setAdapter(infiniteAdapter);
        itemPicker.setSlideOnFling(true);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM)
                .build());

        itemPicker.addOnItemChangedListener(this);
        itemPicker.addScrollStateChangeListener(this);
    }

    private void initAdapterListener(){
        /**
         * @param positionTransfer the real position of the item
         *                         the position has been changed due to the infinite recycle view
         *                         you can just log it and you'll see
         * @param item the current item shown on the screen
         *
         * Here you can define your own custom onItemClick and OnItemLongClick event
         *
         */

        adapter.setOnItemClickListener(new MyAdapter.onItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                dataSize = data.size();
                positionTransfer = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
                item = data.get(positionTransfer);
                image = (ImageView)view.findViewById(R.id.image);
                text = (TextView)view.findViewById(R.id.txt1);
                currentImage = (ImageView) view.findViewById(R.id.image);
//                primarySize=image.getWidth();
                Intent myIntent = new Intent(view.getContext(), Detail.class);
                myIntent.putExtra("myItem",item);

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((Activity)view.getContext()
                                ,image,"activityOption");
                startActivityForResult(myIntent,REQUEST_DETAIL,activityOptionsCompat.toBundle());
//                Log.d("currentImage on longclick=",currentImage.toString());
            }
            @Override
            public void onItemLongClick(View view, int position) {
                //could add some interesting operations here on a long item click
            }
        });

    }


    private void initNavigationMenu(){
        /**
         * @param menu1 menu1 is the item of the PopMenu, and so is menu_i
         * And  add clickListener on each menu, to realize different functionality
         *
         */

        ImageView menu1 = new ImageView(this);

        menu1.setImageResource(R.drawable.remove);
        menu1.setPadding(8,8,8,8);

        ImageView menu2 = new ImageView(this);

        menu2.setImageResource(R.drawable.refresh_circle);
        menu2.setPadding(8,8,8,8);

        ImageView menu3 = new ImageView(this);

        menu3.setImageResource(R.drawable.add);
        menu3.setPadding(8,8,8,8);
        ImageView menu4 = new ImageView(this);

        menu4.setImageResource(R.drawable.locate_circle);
        menu4.setPadding(8,8,8,8);

        FloatingActionButton expended_menu = (FloatingActionButton)findViewById(R.id.expanded_menu);
        SubActionButton.Builder menu_builder = new SubActionButton.Builder(this);

        SubActionButton button1 = menu_builder.setContentView(menu1).build();
        SubActionButton button2 = menu_builder.setContentView(menu2).build();
        SubActionButton button3 = menu_builder.setContentView(menu3).build();
        SubActionButton button4 = menu_builder.setContentView(menu4).build();
        FloatingActionMenu actionMenu  = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(expended_menu).build();



        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int realPosition = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
                delete(realPosition);

            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPicker.setAdapter(infiniteAdapter);
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int realPosition = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
//                delete(realPosition);
                Log.d("menu click ","true");
                Intent myIntent = new Intent(v.getContext(), Detail.class);
                newItem = new Item(10011,"Chistmas","12-25"
                        , "android.resource://com.example.zhu.test4/drawable/dream");
                myIntent.putExtra("myItem",newItem);
                startActivityForResult(myIntent,REQUEST_DETAIL_NEW_ITEM
                        , ActivityOptions.makeSceneTransitionAnimation((Activity)v.getContext()).toBundle());
//                DiscreteScrollViewOptions.smoothScrollToUserSelectedPosition(itemPicker, v,data);
            }
        });

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscreteScrollViewOptions.smoothScrollToUserSelectedPosition(itemPicker, v,data);
            }
        });

    }

    private void initTransition(){
        /**
         *  to achieve activity transition  and sharing element
         */
        final Transition myTransiton=TransitionInflater.from(this).inflateTransition(R.transition.change_bounds);
        Transition myActivityTransition =TransitionInflater.from(this).inflateTransition(R.transition.fade);
        myActivityTransition.excludeTarget(android.R.id.statusBarBackground, true);
        myActivityTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setExitTransition(myActivityTransition);
        getWindow().setReenterTransition(myActivityTransition);
        getWindow().setSharedElementReenterTransition(initSharedElementEnterTransition());
    }

    private void onItemChanged(Item item) {
        /**
         * when sliding the item, the name and date of this item would change automatically
         */

        if(item!=null) {
            currentItemName.setText(item.getName());
            currentItemDate.setText(item.getDate());
            changeRateButtonState(item);
        }
    }


    private void changeRateButtonState(Item item) {
        /**
         * set the alarm status
         * @param Item is the current item
         */
        if (shop.isRated(item.getUri())) {
            rateItemButton.setImageResource(R.drawable.ic_alarm_on_black_24dp);
            rateItemButton.setColorFilter(ContextCompat.getColor(this, R.color.shopRatedStar));
        } else {
            rateItemButton.setImageResource(R.drawable.ic_alarm_on_black_24dp);
            rateItemButton.setColorFilter(ContextCompat.getColor(this, R.color.shopSecondary));
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             *
             * As I mentioned at first, these views are set to be gone, if you want to use
             * them, just go to the avtivity_main.xml to reset the visibility
             *
             * case R.id.item_btn_buy:
             * DiscreteScrollViewOptions.smoothScrollToUserSelectedPosition(itemPicker, v,data);
             * break;
             * case R.id.item_btn_newItem:
             * ActivityCompat.requestPermissions(MainActivity.this, mPermissionList, 100);
             * itemPicker.setAdapter(infiniteAdapter);
             * break;
             * case R.id.item_btn_remove:
             * realPosition = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
             * delete(realPosition);
             * break;
             */
            case R.id.item_btn_rate:
                int realPosition = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
                Item current = data.get(realPosition);

                shop.setRated(current.getUri(), !shop.isRated(current.getUri()));
                changeRateButtonState(current);
                break;

            default:
                showUnsupportedSnackBar();
                break;
        }
    }



    public void delete(int position){
        /**
         * this delete function is for deleting items
         * but at least one item should be kept in case some unknown exceptions would happen
         * @param position is the real position , i.e., the index of the item
         */
       if(data.size()>1){
//           Log.d("delete in",position+"");
           data.remove(position);
//           Log.d("delete in more",position+"");
           infiniteAdapter.notifyItemRemoved(position);
           infiniteAdapter.notifyItemRangeChanged(0,data.size());
           itemPicker.setAdapter(infiniteAdapter);
       }

    }


    @Override
    public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable MyAdapter.ViewHolder currentHolder, @Nullable MyAdapter.ViewHolder newCurrent) {

        int realPosition = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
        int realNewPosition = infiniteAdapter.getRealPosition(newPosition);

        if (newPosition>= 0 && newPosition < itemPicker.getAdapter().getItemCount()) {
//            Item next = data.get(newPosition);
            if(Math.abs(newPosition-currentPosition)==1) gradientView.onScroll(1f - Math.abs(scrollPosition),realPosition,realNewPosition);
            currentItemName.setScaleX(1f - Math.abs(scrollPosition));
            currentItemName.setScaleY(1f - Math.abs(scrollPosition));
            currentItemDate.setScaleX(1f - Math.abs(scrollPosition));
            currentItemDate.setScaleY(1f - Math.abs(scrollPosition));
        }

    }
    @Override
    public void onScrollStart(@NonNull MyAdapter.ViewHolder currentItemHolder, int adapterPosition) { }
    @Override
    public void onScrollEnd(@NonNull MyAdapter.ViewHolder currentItemHolder, int adapterPosition) { }
    private void showUnsupportedSnackBar() {
        Snackbar.make(itemPicker, "wrong operations", Snackbar.LENGTH_SHORT).show();
    }



    @Override
    public void onCurrentItemChanged(@Nullable MyAdapter.ViewHolder viewHolder, int adapterPosition) {
        /**
         * To make animations of the current date and name
         */
        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        onItemChanged(data.get(positionInDataSet));
        //make animations of the itemdata and itemname
        currentItemDate.animate()
                .scaleX(1f).scaleY(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(400)
                .start();
        currentItemName.animate()
                .scaleX(1f).scaleY(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300)
                .start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent datas) {
        /**
         * This is to receive the request result from the detail activity
         */
        super.onActivityResult(requestCode, resultCode, datas);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //this case is the return when creating new item
                case REQUEST_DETAIL_NEW_ITEM:
                    Log.d("currentImage=",String.valueOf(REQUEST_DETAIL_NEW_ITEM));
                    if(datas!=null) {
                        result_date = datas.getStringExtra("date");
                        result_name = datas.getStringExtra("name");
                        if(datas.getStringExtra("uri")!=null){
                            result_uri = datas.getStringExtra("uri");
                            newItem.setUri(result_uri);
//                            View view = LayoutInflater.from(item).inflate(R.layout.item_presion,parent, false);
                        }

                        if(result_date!=null){
                            newItem.setDate(result_date);
                        }

                        if(result_name.length()>=1)
                            newItem.setName(result_name);
                        data.add(newItem);
                        infiniteAdapter.notifyItemInserted(1);
                        infiniteAdapter.notifyItemRangeChanged(1,data.size()-1);
                    }
                    break;
                //this case is the return when changing the card
                case REQUEST_DETAIL:
                    if(datas!=null){
//                        Log.d("Detail=","Back");
                        result_date = datas.getStringExtra("date");
                        result_name = datas.getStringExtra("name");
                        if(datas.getStringExtra("uri")!=null){
                            result_uri = datas.getStringExtra("uri");
                            item.setUri(result_uri);
//                          Log.d("currentImage=",currentImage.toString());
                        }
                        if(result_date!=null){
                            item.setDate(result_date);
                        }
                        if(result_name.length()>=1)
                            item.setName(result_name);

                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .error(R.drawable.shop1)
                                .dontAnimate()
                                .priority(Priority.HIGH)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true);
                        onItemChanged(item);
                    }
                    break;
                default:
                    Log.d("Activity for Result=","WRONG");
            }
        }
    }

    private Transition initSharedElementEnterTransition() {
        /**
         * I have a shared Element(the image of each item)
         * This is to set some configuration
         */
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
    Transition getTransition() {
        return new ChangeImageTransform();
    }

}





