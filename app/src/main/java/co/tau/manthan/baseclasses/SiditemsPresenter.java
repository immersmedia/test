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


public class SiditemsPresenter extends RecyclerViewPresenter<Siditem> {

    @SuppressWarnings("WeakerAccess")
    protected Adapter adapter;
    private List<Siditem> itemlist = new ArrayList<>();
    private Context cntxt;

    @SuppressWarnings("WeakerAccess")
    public SiditemsPresenter(@NonNull Context context, List<Siditem> itemlist) {
        super(context);
        this.itemlist = itemlist;
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
        List<Siditem> all = itemlist;

        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<Siditem> list = new ArrayList<>();
            for (Siditem u : all) {
                if (u.getName().toLowerCase().contains(query) ||
                        u.getSid().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("SiditemPresenter", "found "+list.size()+" Siditems for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    protected class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<Siditem> data = itemlist;

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
        protected void setData(@Nullable List<Siditem> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(cntxt).inflate(R.layout.hashtaglo, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            if (isEmpty()) {
                holder.vhashtaginfull.setText("No user here!");
                holder.vhashtagstring.setText("Sorry!");
                holder.root.setOnClickListener(null);
                return;
            }
            final Siditem Siditem = data.get(position);
            holder.vhashtaginfull.setText(Siditem.getName());
            holder.vhashtagstring.setText("@" + Siditem.getSid());
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(Siditem);
                }
            });
        }
    }
}
