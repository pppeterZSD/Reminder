package com.example.zhu.test4;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
/**
 * This is the custom adapter of RecycleView
 *  @author zhusd
 *  @version 1.0
 */

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {

        private List<Item> data;

        public MyAdapter(List<Item> data) {

            this.data = data;
        }
        private onItemClickListener onItemClickListener;
        public void setOnItemClickListener(onItemClickListener onItemClickListener){
            this.onItemClickListener=onItemClickListener;
        }

        public interface  onItemClickListener{
            void onItemClick(View view ,int position);
            void  onItemLongClick(View view,int position);
        }



        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
//            Log.d("","onBindViewHolder: verify reuse");
//            Log.d( "","onBindViewHolder: reuse"+holder.itemView.getTag());
//            Log.d("","onBindViewHolder: put at"+data.get(position).getUri());

            final String uri = data.get(position).getUri();
            holder.itemView.setTag(data.get(position).getUri());

            if(uri.equals(holder.itemView.getTag())){
                Glide.with(holder.itemView.getContext())
                        .load(data.get(position).getUri())
                        .into(holder.image);
                holder.getTextView().setText((data.get(position)).getName());
            }

            holder.getTextView().setText((data.get(position)).getName());
            if(onItemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                           int layoutPos=holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView,layoutPos);

                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int layoutPos=holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView,layoutPos);
                        return false;
                    }
                });
            }

        }

        @Override
        public int getItemCount() {

            if(data.size()==0) return 0;
            return data.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.cardview, parent, false);
            return new ViewHolder(v);
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView image;
            private TextView txt;
            public ViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.image);
                txt = (TextView) itemView.findViewById(R.id.txt1);
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        txt.setText("clicked");
//                    }
//                });
            }

            public TextView getTextView() {
                return txt;
            }



        }

    }

