package com.levent.pia;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    int pos;

    public SliderAdapter(Context context) {

        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);

        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        pos=position;
        switch (position) {
            case 0:
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.manti)
                        .into(viewHolder.imageViewBackground);


                break;
            case 1:
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.citir)
                        .into(viewHolder.imageViewBackground);

                break;

            case 2:
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.zyagli)
                        .into(viewHolder.imageViewBackground);

                break;

            case 3:
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.gozleme)
                        .into(viewHolder.imageViewBackground);

                break;

            case 4:
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.cigborek)
                        .into(viewHolder.imageViewBackground);

                break;

            case 5:
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.iclikofte)
                        .into(viewHolder.imageViewBackground);

                break;

            case 6:
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.schnitzel)
                        .into(viewHolder.imageViewBackground);

                break;

                case 7:
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.etliyaprak)
                        .into(viewHolder.imageViewBackground);

                break;



            default:
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.manti)
                        .into(viewHolder.imageViewBackground);

                break;


        }

    }


    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 8;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(final View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.scrollone);
            this.itemView = itemView;

                 }
    }


}
