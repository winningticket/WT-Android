package com.winningticketproject.in.ChatWithOther;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.winningticketproject.in.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myzupp on 08-04-2017.
 *
 * @author Darshan Parikh (parikhdarshan36@gmail.com)
 */

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {

    private Context context;
    private List<chat_data> chat_data = new ArrayList<>();
    private Chat_With_Other mainActivity;

    public AudioListAdapter(Context context, List<chat_data> chat_data) {
        this.context = context;
        this.chat_data = chat_data;
        this.mainActivity = (Chat_With_Other) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_with_others, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.user_type = chat_data.get(position).user_type;
        holder.storage = FirebaseStorage.getInstance();
        holder.storageReference = holder.storage.getReference();

        holder.mime_type = chat_data.get(position).mime_type;
        holder.time = chat_data.get(position).timestamp;
        holder.user_attachment = chat_data.get(position).attachment_utl;
        holder.message = chat_data.get(position).message;

        if(holder.user_type.equals("1")) {
            holder.msg_time.setGravity(Gravity.RIGHT);
            holder.image_msg_time.setGravity(Gravity.RIGHT);
            holder.txt_msg.setGravity(Gravity.RIGHT);
            holder.textlayout.setGravity(Gravity.RIGHT);
            holder.imagelayout.setGravity(Gravity.RIGHT);
            holder.audio_player.setGravity(Gravity.RIGHT);
            holder.audio_msg_time.setGravity(Gravity.RIGHT);
            holder.ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_green_24dp);
            holder.txt_msg.setBackgroundResource(R.drawable.bubble_my_message);
            holder.layout_player2.setBackgroundResource(R.drawable.bubble_my_message);
            holder.imageView_attachment.setBackgroundResource(R.drawable.bubble_my_message);
            holder.seekBarAudio.getProgressDrawable().setColorFilter(Color.rgb(32,198,192), PorterDuff.Mode.SRC_IN);
            holder.seekBarAudio.getThumb().setColorFilter(Color.rgb(32,198,192), PorterDuff.Mode.SRC_IN);

        } else{
            holder.txt_msg.setBackgroundResource(R.drawable.bubble_there_message);
            holder.imageView_attachment.setBackgroundResource(R.drawable.bubble_there_message);
            holder.layout_player2.setBackgroundResource(R.drawable.bubble_there_message);
            holder.ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
            holder.audio_player.setGravity(Gravity.LEFT);
            holder.txt_msg.setGravity(Gravity.LEFT);
            holder.textlayout.setGravity(Gravity.LEFT);
            holder.imagelayout.setGravity(Gravity.LEFT);
            holder.msg_time.setGravity(Gravity.LEFT);
            holder.image_msg_time.setGravity(Gravity.LEFT);
            holder.audio_msg_time.setGravity(Gravity.LEFT);
            holder.seekBarAudio.getProgressDrawable().setColorFilter(Color.rgb(0,177,227), PorterDuff.Mode.SRC_IN);
            holder.seekBarAudio.getThumb().setColorFilter(Color.rgb(0,177,227), PorterDuff.Mode.SRC_IN);
        }


        if (holder.mime_type.equals("image/jpeg")){
            holder.image_msg_time.setText(holder.time.replace("AM", "am").replace("PM","pm"));
            Uri uri = Uri.parse(holder.user_attachment);
            String path = uri.getLastPathSegment();
            holder.storageReference.child("images/"+path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    System.out.println("-----------"+uri);
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(context /* context */)
                            .load(uri)
                            .into(holder.imageView_attachment);

                    holder.imageView_attachment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(holder.isImageFitToScreen) {
                                holder.isImageFitToScreen=false;
                                holder.imageView_attachment.setLayoutParams(new LinearLayout.LayoutParams(440,420));
                                holder.imageView_attachment.setScaleType(ImageView.ScaleType.FIT_XY);
                            }else{
                                holder.isImageFitToScreen=true;
                                holder.imageView_attachment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                holder.imageView_attachment.setScaleType(ImageView.ScaleType.FIT_XY);
                            }

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            holder.audio_player.setVisibility(View.GONE);
            holder.txt_msg.setVisibility(View.GONE);
            holder.textlayout.setVisibility(View.GONE);
            holder.imagelayout.setVisibility(View.VISIBLE);

        }else  if (holder.mime_type.equals("audio/mp4")){
            holder.audio_msg_time.setText(holder.time.replace("AM", "am").replace("PM","pm"));
            holder.txt_msg.setVisibility(View.GONE);
            holder.textlayout.setVisibility(View.GONE);
            holder.imagelayout.setVisibility(View.GONE);
            holder.audio_player.setVisibility(View.VISIBLE);

            if(mainActivity.audioStatusList.get(position).getAudioState() != AudioStatus.AUDIO_STATE.IDLE.ordinal()) {
                holder.seekBarAudio.setMax(mainActivity.audioStatusList.get(position).getTotalDuration());
                holder.seekBarAudio.setProgress(mainActivity.audioStatusList.get(position).getCurrentValue());
                holder.seekBarAudio.setEnabled(true);
            } else {
                holder.seekBarAudio.setProgress(0);
                holder.seekBarAudio.setEnabled(false);
            }

            if(mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.IDLE.ordinal() || mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.PAUSED.ordinal()) {
                if(holder.user_type.equals("1")) {
                    holder.ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_green_24dp);
                }else {
                    holder.ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                }
            } else {

                if(holder.user_type.equals("1")) {
                    holder.ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_green_24dp);
                }else {
                    holder.ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        }else{
            holder.textlayout.setVisibility(View.VISIBLE);
            holder.audio_player.setVisibility(View.GONE);
            holder.txt_msg.setText(holder.message);
            holder.msg_time.setText(holder.time.replace("AM", "am").replace("PM","pm"));
            holder.imagelayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chat_data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sbProgress)
        SeekBar seekBarAudio;

        @BindView(R.id.ivPlayPause)
        ImageView ivPlayPause;

        @BindView(R.id.txt_msg)
        TextView txt_msg;

        @BindView(R.id.msg_time)
        TextView msg_time;

        @BindView(R.id.image_msg_time)
        TextView image_msg_time;

        @BindView(R.id.audio_msg_time)
        TextView audio_msg_time;

        @BindView(R.id.textlayout)
        LinearLayout textlayout;

        @BindView(R.id.imagelayout)
        LinearLayout imagelayout;

        @BindView(R.id.audio_player)
        LinearLayout audio_player;

        @BindView(R.id.layout_2)
        LinearLayout layout_player2;

        @BindView(R.id.user_attachment)
        ImageView imageView_attachment;

        String user_type;

        FirebaseStorage storage;
        StorageReference storageReference;

        boolean isImageFitToScreen;

        String message;

        String mime_type;

        String time ;
        String user_attachment;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser) MediaPlayerUtils.applySeekBarValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            ivPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean ifRequest = mainActivity.requestPermissionIfNeeded();
                    if(ifRequest) return;

                    int position = getAdapterPosition();

                    // Check if any other audio is playing
                    if(mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.IDLE.ordinal()) {
                        // Reset media player
                        MediaPlayerUtils.Listener listener = (MediaPlayerUtils.Listener) context;
                        listener.onAudioComplete();
                    }

                    Uri uri=Uri.fromFile(new File(user_attachment));
                    String path = uri.getLastPathSegment();
                    storageReference.child("audio/" + path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Check if any other audio is playing
                            if (mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.IDLE.ordinal()) {
                                // Reset media player
                                MediaPlayerUtils.Listener listener = (MediaPlayerUtils.Listener) context;
                                listener.onAudioComplete();
                            }

                            AudioStatus audioStatus = mainActivity.audioStatusList.get(position);
                            int currentAudioState = audioStatus.getAudioState();

                            if (currentAudioState == AudioStatus.AUDIO_STATE.PLAYING.ordinal()) {
                                // If mediaPlayer is playing, pause mediaPlayer

                                if(user_type.equals("1")) {
                                    ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_green_24dp);
                                }else {
                                    ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                                }

                                MediaPlayerUtils.pauseMediaPlayer();

                                audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PAUSED.ordinal());
                                mainActivity.audioStatusList.set(position, audioStatus);
                            } else if (currentAudioState == AudioStatus.AUDIO_STATE.PAUSED.ordinal()) {
                                // If mediaPlayer is paused, play mediaPlayer

                                if(user_type.equals("1")) {
                                    ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_green_24dp);
                                }else {
                                    ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                                }

                                MediaPlayerUtils.playMediaPlayer();

                                audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PLAYING.ordinal());
                                mainActivity.audioStatusList.set(position, audioStatus);
                            } else {
                                // If mediaPlayer is in idle state, start and play mediaPlayer

                                if(user_type.equals("1")) {
                                    ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_green_24dp);
                                }else {
                                    ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                                }
                                audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PLAYING.ordinal());
                                mainActivity.audioStatusList.set(position, audioStatus);

                                try {
                                    MediaPlayerUtils.startAndPlayMediaPlayer(uri.toString(), (MediaPlayerUtils.Listener) context);

                                    audioStatus.setTotalDuration(MediaPlayerUtils.getTotalDuration());
                                    mainActivity.audioStatusList.set(position, audioStatus);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            });
        }
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
