package top.spencer.crabscore.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import top.spencer.crabscore.R;
import top.spencer.crabscore.common.CommonConstant;
import top.spencer.crabscore.model.entity.User;

import java.util.List;
import java.util.Objects;

/**
 * 用户管理页面用户列表适配器
 *
 * @author spencercjh
 */
public class UserAdminListAdapter extends RecyclerView.Adapter<UserListItemViewHolder> {
    private MyOnItemClickListener myOnItemClickListener;
    private List<User> userList;
    private Context context;

    public UserAdminListAdapter(List<User> data, Context context) {
        this.userList = data;
        this.context = context;
    }

    public void setOnItemClickListener(MyOnItemClickListener mListener) {
        this.myOnItemClickListener = mListener;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @SuppressWarnings("Duplicates")
    @NonNull
    @Override
    public UserListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_admin, parent, false);
        UserListItemViewHolder userListItemViewHolder = new UserListItemViewHolder(v);
        if (myOnItemClickListener != null) {
            userListItemViewHolder.itemView.setOnClickListener(v1 -> myOnItemClickListener.onItemClick(v1));
            userListItemViewHolder.itemView.setOnLongClickListener(v12 -> {
                myOnItemClickListener.onItemLongClick(v12);
                return true;
            });
        }
        return userListItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListItemViewHolder holder, int position) {
        if (userList.get(position) != null) {
            User user = userList.get(position);
            holder.itemView.setTag(user);
            holder.username.setText(user.getUserName());
            holder.displayName.setText(user.getDisplayName());
            holder.phone.setText(user.getEmail());
            Glide.with(Objects.requireNonNull(context))
                    .load(user.getAvatarUrl())
                    .into(holder.avatar);
            if (user.getStatus().equals(CommonConstant.USER_STATUS_NORMAL)) {
                holder.status.setText(user.getCompetitionId().equals(CommonConstant.USER_COMPETITION_ALL) ?
                        "永久有效" : "仅当前赛事有效");
            } else if (user.getStatus().equals(CommonConstant.USER_STATUS_LOCK)) {
                holder.status.setText("未启用");
            }
            if (user.getRoleId().equals(CommonConstant.USER_TYPE_ADMIN)) {
                holder.role.setText("管理员");
            } else if (user.getRoleId().equals(CommonConstant.USER_TYPE_JUDGE)) {
                holder.role.setText("评委");
            } else if (user.getRoleId().equals(CommonConstant.USER_TYPE_STAFF)) {
                holder.role.setText("工作人员");
            } else if (user.getRoleId().equals(CommonConstant.USER_TYPE_COMPANY)) {
                holder.role.setText("参选单位");
            }
        }
    }
}
