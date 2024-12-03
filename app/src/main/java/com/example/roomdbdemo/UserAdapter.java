package com.example.roomdbdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHoler>{

    private List<User> ulist;
    private IClickItemUser iClickItemUser;
    public interface IClickItemUser{
        void updateUser(User user);
        void deleteUser(User user);
    }

    public UserAdapter(IClickItemUser iClickItemUser) {
        this.iClickItemUser = iClickItemUser;
    }

    public void setData(List<User> list){
        this.ulist = list;
        notifyDataSetChanged(); // load du lieu len recycleview
    }

    @NonNull
    @Override
    public UserViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHoler holder, int position) {
        User user = ulist.get(position);
        if(user == null){
            return;
        }

        holder.tv_userName.setText(user.getUserName());
        holder.tv_address.setText(user.getAddress());
        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemUser.updateUser(user);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemUser.deleteUser(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ulist != null){
            return ulist.size();
        }
        return 0;
    }

    public class UserViewHoler extends RecyclerView.ViewHolder {
        private TextView tv_userName;
        private TextView tv_address;
        private  Button btn_update;
        private Button btn_delete;

        public UserViewHoler(@NonNull View itemView) {
            super(itemView);

            tv_userName = itemView.findViewById(R.id.tv_userName);
            tv_address = itemView.findViewById(R.id.tv_address);
            btn_update = itemView.findViewById(R.id.btn_update);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
