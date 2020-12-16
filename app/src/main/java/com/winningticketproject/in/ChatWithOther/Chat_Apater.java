package com.winningticketproject.in.ChatWithOther;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

public class Chat_Apater extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    List<chat_data> chat_data;
    private Context mContext;
    MediaPlayer mediaPlayer;
    boolean isImageFitToScreen;
    private Handler handler;
    Runnable runnable;
    private  Chat_With_Other mainActivity;


    public Chat_Apater(Context context, List<chat_data> chat_data) {
        this.chat_data = chat_data;
        this.mContext = context;
        handler = new Handler();
        mediaPlayer = new MediaPlayer();
        this.mainActivity = (Chat_With_Other) context;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewTWO = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_with_others, parent, false);
        return new Chat_Apater.CustomViewHolderlist(viewTWO);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Chat_Apater.CustomViewHolderlist) holder).bindData(position,chat_data.get(position).message, chat_data.get(position).user_type, chat_data.get(position).attachment_utl, chat_data.get(position).mime_type,chat_data.get(position).timestamp);
    }

    public class CustomViewHolderlist extends RecyclerView.ViewHolder {
        View view;
        TextView txt_msg,msg_time,image_msg_time,audio_msg_time;
        LinearLayout textlayout,imagelayout,audio_player,layout_player2;
        ImageView imageView_attachment;
        FirebaseStorage storage;
        SeekBar sbProgress;
        StorageReference storageReference;
        ImageView ivPlayPause;
        public CustomViewHolderlist(View itemView) {
            super(itemView);
            view = itemView;
            txt_msg = view.findViewById(R.id.txt_msg);
            audio_msg_time = view.findViewById(R.id.audio_msg_time);
            image_msg_time = view.findViewById(R.id.image_msg_time);
            textlayout = view.findViewById(R.id.textlayout);
            imagelayout = view.findViewById(R.id.imagelayout);

            audio_player = view.findViewById(R.id.audio_player);
            layout_player2 = view.findViewById(R.id.layout_2);

            sbProgress = view.findViewById(R.id.sbProgress);

            msg_time = view.findViewById(R.id.msg_time);

            ivPlayPause = view.findViewById(R.id.ivPlayPause);

            imageView_attachment = view.findViewById(R.id.user_attachment);

        }


        public void bindData(int position,final String message, final String user_type, final String user_attachment, String mime_type,String time) {
            // get the Firebase  storage reference
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            if(user_type.equals("1")) {
                msg_time.setGravity(Gravity.RIGHT);
                image_msg_time.setGravity(Gravity.RIGHT);
                txt_msg.setGravity(Gravity.RIGHT);
                textlayout.setGravity(Gravity.RIGHT);
                imagelayout.setGravity(Gravity.RIGHT);
                audio_player.setGravity(Gravity.RIGHT);
                audio_msg_time.setGravity(Gravity.RIGHT);
                ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_green_24dp);
                txt_msg.setBackgroundResource(R.drawable.bubble_my_message);
                layout_player2.setBackgroundResource(R.drawable.bubble_my_message);
                imageView_attachment.setBackgroundResource(R.drawable.bubble_my_message);
                sbProgress.getProgressDrawable().setColorFilter(Color.rgb(32,198,192), PorterDuff.Mode.SRC_IN);
                sbProgress.getThumb().setColorFilter(Color.rgb(32,198,192), PorterDuff.Mode.SRC_IN);

            } else{
                txt_msg.setBackgroundResource(R.drawable.bubble_there_message);
                imageView_attachment.setBackgroundResource(R.drawable.bubble_there_message);
                layout_player2.setBackgroundResource(R.drawable.bubble_there_message);
                ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                audio_player.setGravity(Gravity.LEFT);
                txt_msg.setGravity(Gravity.LEFT);
                textlayout.setGravity(Gravity.LEFT);
                imagelayout.setGravity(Gravity.LEFT);
                msg_time.setGravity(Gravity.LEFT);
                image_msg_time.setGravity(Gravity.LEFT);
                audio_msg_time.setGravity(Gravity.LEFT);
                sbProgress.getProgressDrawable().setColorFilter(Color.rgb(0,177,227), PorterDuff.Mode.SRC_IN);
                sbProgress.getThumb().setColorFilter(Color.rgb(0,177,227), PorterDuff.Mode.SRC_IN);
            }

            if (mime_type.equals("image/jpeg")){
                image_msg_time.setText(time.replace("AM", "am").replace("PM","pm"));
                Uri uri = Uri.parse(user_attachment);
                String path = uri.getLastPathSegment();
                storageReference.child("images/"+path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("-----------"+uri);
                        // Got the download URL for 'users/me/profile.png'
                        Glide.with(mContext /* context */)
                                .load(uri)
                                .into(imageView_attachment);

                        imageView_attachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(isImageFitToScreen) {
                                    isImageFitToScreen=false;
                                    imageView_attachment.setLayoutParams(new LinearLayout.LayoutParams(440,420));
                                    imageView_attachment.setScaleType(ImageView.ScaleType.FIT_XY);
                                }else{
                                    isImageFitToScreen=true;
                                    imageView_attachment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                    imageView_attachment.setScaleType(ImageView.ScaleType.FIT_XY);
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
                audio_player.setVisibility(View.GONE);
                txt_msg.setVisibility(View.GONE);
                textlayout.setVisibility(View.GONE);
                imagelayout.setVisibility(View.VISIBLE);

            }else  if (mime_type.equals("audio/mp4")){
                audio_msg_time.setText(time.replace("AM", "am").replace("PM","pm"));
                txt_msg.setVisibility(View.GONE);
                textlayout.setVisibility(View.GONE);
                imagelayout.setVisibility(View.GONE);
                audio_player.setVisibility(View.VISIBLE);

                if(mainActivity.audioStatusList.get(position).getAudioState() != AudioStatus.AUDIO_STATE.IDLE.ordinal()) {
                    sbProgress.setMax(mainActivity.audioStatusList.get(position).getTotalDuration());
                    sbProgress.setProgress(mainActivity.audioStatusList.get(position).getCurrentValue());
                    sbProgress.setEnabled(true);
                } else {
                    sbProgress.setProgress(0);
                    sbProgress.setEnabled(false);
                }

                if(mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.IDLE.ordinal() || mainActivity.audioStatusList.get(position).getAudioState() == AudioStatus.AUDIO_STATE.PAUSED.ordinal()) {
                    if(user_type.equals("1")) {
                        ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_green_24dp);
                    }else {
                        ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                    }
                } else {

                    if(user_type.equals("1")) {
                        ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_green_24dp);
                    }else {
                        ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    }
                }

                sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser) MediaPlayerUtils.applySeekBarValue(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) { }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }
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
                            MediaPlayerUtils.Listener listener = (MediaPlayerUtils.Listener) mContext;
                            listener.onAudioComplete();
                        }


                        Uri uri=Uri.fromFile(new File(user_attachment));
                            String path = uri.getLastPathSegment();
                            storageReference.child("audio/" + path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String audioPath = uri.getPath();

                                    AudioStatus audioStatus = mainActivity.audioStatusList.get(position);
                                    int currentAudioState = audioStatus.getAudioState();

                                    if(currentAudioState == AudioStatus.AUDIO_STATE.PLAYING.ordinal()) {
                                        // If mediaPlayer is playing, pause mediaPlayer

                                        if(user_type.equals("1")) {
                                            ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_green_24dp);
                                        }else {
                                            ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                                        }

                                        MediaPlayerUtils.pauseMediaPlayer();

                                        audioStatus.setAudioState(AudioStatus.AUDIO_STATE.PAUSED.ordinal());
                                        mainActivity.audioStatusList.set(position, audioStatus);
                                    } else if(currentAudioState == AudioStatus.AUDIO_STATE.PAUSED.ordinal()) {
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
                                            MediaPlayerUtils.startAndPlayMediaPlayer(audioPath, (MediaPlayerUtils.Listener) mContext);

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

//                ivPlayPause.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if( mediaPlayer != null && mediaPlayer.isPlaying())
//                        {
//                            System.out.println("------playing stop-----");
//                            is_playing = false;
//                            if(user_type.equals("1")) {
//                                ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_green_24dp);
//                            }else {
//                                ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
//                            }
//                            mediaPlayer.stop();
//                            mediaPlayer.release();
//                            mediaPlayer = null;
//
//                        }else {
//                            System.out.println("------playing-----");
//
//                            Uri uri=Uri.fromFile(new File(user_attachment));
//                            String path = uri.getLastPathSegment();
//                            storageReference.child("audio/" + path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    is_playing = true;
//                                    ChangeSeekbar();
//                                    sbProgress.setEnabled(true);
//                                    if(user_type.equals("1")) {
//                                        ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_green_24dp);
//                                    }else {
//                                        ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
//                                    }
//
//                                    try {
//                                        final String url = uri.toString();
//
//                                        System.out.println("-------audio--------"+url);
//
//                                        mediaPlayer = new MediaPlayer();
//                                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                                        mediaPlayer.setDataSource(url);
//                                        mediaPlayer.prepareAsync();
//                                        mediaPlayer.start();
//                                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                            @Override
//                                            public void onPrepared(MediaPlayer mp) {
//                                                sbProgress.setMax(mp.getDuration());
//                                                mp.start();
//                                                ChangeSeekbar();
//                                                sbProgress.setProgress(mp.getCurrentPosition());
//
//                                                System.out.println("-------audio due--------"+mp.getDuration());
//                                            }
//                                        });
//
//                                        if(mediaPlayer.isPlaying()) {
//                                            mediaPlayer.seekTo(0);
//                                            mediaPlayer.pause();
//                                            if(user_type.equals("1")) {
//                                                ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_green_24dp);
//                                            }else {
//                                                ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
//                                            }
//                                        }
//
//                                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                                            @Override
//                                            public boolean onError(MediaPlayer mp, int what, int extra) {
//                                                return false;
//                                            }
//                                        });
//
//                                        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                                            @Override
//                                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                                                if (b) {
//                                                    if ( mediaPlayer.isPlaying())
//                                                        mediaPlayer.seekTo(i);
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onStartTrackingTouch(SeekBar seekBar) {
//
//                                            }
//
//                                            @Override
//                                            public void onStopTrackingTouch(SeekBar seekBar) {
//
//                                            }
//                                        });
//                                    }catch (IllegalStateException | IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    // Handle any errors
//                                }
//                            });
//                        }
//                    }
//
//                    private void ChangeSeekbar() {
//                        sbProgress.setEnabled(true);
//                        if( mediaPlayer != null && !mediaPlayer.isPlaying()) {
//                        sbProgress.setProgress(mediaPlayer.getCurrentPosition());
//                        runnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                ChangeSeekbar();
//                            }
//                        };
//                        handler.postDelayed(runnable,1000);
//                        }
//                    }
//
//
//                });

            }else{
                textlayout.setVisibility(View.VISIBLE);
                audio_player.setVisibility(View.GONE);
                txt_msg.setText(message);
                msg_time.setText(time.replace("AM", "am").replace("PM","pm"));
                imagelayout.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public int getItemCount() {
        return chat_data.size();
    }

}