package com.example.admin.trackme.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.trackme.R;
import com.example.admin.trackme.model.Session;

import java.util.List;


public class AdapterSession extends RecyclerView.Adapter<AdapterSession.RecyclerViewHolder>{

    private Context mContext;
    private List<Session> dataSession;

    public AdapterSession(Context mContext, List<Session> dataSession) {
        this.mContext = mContext;
        this.dataSession = dataSession;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.session_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        Session session = dataSession.get(position);

        holder.distance.setText(session.getDistance());
        holder.avgSpeed.setText(session.getAverageSpeed());
        holder.time.setText((session.getTime()));
        Bitmap bmMap = BitmapFactory.decodeByteArray(session.getImage(),0,session.getImage().length);
        holder.imageMapView.setImageBitmap(bmMap);
    }

    @Override
    public int getItemCount() {
        return dataSession.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private View itemview;
        TextView distance;
        TextView avgSpeed;
        TextView time;
        ImageView imageMapView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            distance = itemView.findViewById(R.id.tvDistance);
            avgSpeed = itemView.findViewById(R.id.tvAvgSpeed);
            time = itemView.findViewById(R.id.tvTime);
            imageMapView = itemView.findViewById(R.id.imageMapView);
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(String username);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
