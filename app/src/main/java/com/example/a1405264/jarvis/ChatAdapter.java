package com.example.a1405264.jarvis;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    Activity context;
    List<ChatModel> chatModels;
    int temp=-1;

    public ChatAdapter(Activity context, List<ChatModel> chatModels) {
        this.context = context;
        this.chatModels = chatModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if(viewType==temp) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_activity_send_adapter_layout, parent, false);
        }
        else
        {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_activity_receive_adapter_layout, parent, false);
        }
        return new ViewHolder(v);
    }
    @Override
    public int getItemViewType(int position) {
        ChatModel chat=chatModels.get(position);
        if (chat.getFrom()==0)
        {
            return temp;
        }
        else
            return position;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ChatModel chatModel=chatModels.get(position);
        holder.text.setText(chatModel.getMessage());
        if(chatModel.getAction()==100)
        {
            holder.webView.setVisibility(View.VISIBLE);
            //holder.webView.loadData(chatModel.getExtraMessage(), "text/html", null);
            holder.webView.loadUrl(chatModel.getExtraMessage());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chatModel.getExtraMessage().length()>0)
                {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(chatModel.getExtraMessage()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        context.startActivity(intent);
                    }
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text;
        WebView webView;
        public ViewHolder(View itemView) {
            super(itemView);
            text=(TextView)itemView.findViewById(R.id.chat_message_text);
            webView=(WebView)itemView.findViewById(R.id.web);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDisplayZoomControls(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setGeolocationEnabled(true);
        }
    }
}
