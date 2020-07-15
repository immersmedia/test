package co.tau.manthan.baseclasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.ArrayList;
import java.util.List;

import co.tau.manthan.R;


public class HashtagPresenter extends RecyclerViewPresenter<Hashtag> {

    @SuppressWarnings("WeakerAccess")
    protected Adapter adapter;
    private List<Hashtag> hashtaglist = new ArrayList<>();
    private Context cntxt;

    @SuppressWarnings("WeakerAccess")
    public HashtagPresenter(@NonNull Context context, List<Hashtag> hashtagList) {
        super(context);
        this.hashtaglist = hashtagList;
        this.cntxt = context;

    }

    @NonNull
    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @NonNull
    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        List<Hashtag> all = hashtaglist;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<Hashtag> list = new ArrayList<>();
            for (Hashtag u : all) {
                if (u.getHashtaginfull().toLowerCase().contains(query) ||
                        u.getHashtagstring().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("HashtagPresenter", "found "+list.size()+" Hashtags for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    protected class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<Hashtag> data = hashtaglist;

        @SuppressWarnings("WeakerAccess")
        protected class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView vhashtaginfull;
            private TextView vhashtagstring;
            Holder(View itemView) {
                super(itemView);
                root = itemView;
                vhashtaginfull = itemView.findViewById(R.id.hashtaginfull);
                vhashtagstring = itemView.findViewById(R.id.hashtagstring);
            }
        }

        @SuppressWarnings("WeakerAccess")
        protected void setData(@Nullable List<Hashtag> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
             return (isEmpty()) ? 1 : hashtaglist.size();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(cntxt).inflate(R.layout.hashtaglo, parent, false);
            return new Holder(itemview);
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
             if (isEmpty()) {
                holder.vhashtaginfull.setText("No Hashtag here!");
                holder.vhashtagstring.setText("Sorry!");
                holder.root.setOnClickListener(null);
                return;
            }
            if (position > data.size()-1){
                return;
                //MANJU - This needs to be investigated further.
            }
            final Hashtag hashtag = data.get(position);
            int htagcolor = Hashtag.gethashtagtocolor(hashtag.getHashtagstring());
            holder.vhashtaginfull.setText(hashtag.getHashtaginfull());
            holder.vhashtagstring.setText("#" + hashtag.getHashtagstring());

            holder.vhashtaginfull.setTextColor(getContext().getResources().getColor(htagcolor));
            holder.vhashtagstring.setTextColor(getContext().getResources().getColor(htagcolor));

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(hashtag);
                }
            });
        }
    }
}
