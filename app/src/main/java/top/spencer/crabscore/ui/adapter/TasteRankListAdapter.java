package top.spencer.crabscore.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import top.spencer.crabscore.R;
import top.spencer.crabscore.model.entity.vo.GroupResult;

import java.util.List;
import java.util.Objects;

/**
 * 口感分数排名列表适配器
 *
 * @author spencercjh
 */
public class TasteRankListAdapter extends RecyclerView.Adapter<RankListItemViewHolder> {

    private List<GroupResult> groupList;
    private Context context;

    public TasteRankListAdapter(List<GroupResult> data, Context context) {
        this.groupList = data;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @NonNull
    @Override
    public RankListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score_rank, parent, false);
        return new RankListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RankListItemViewHolder holder, int position) {
        holder.order.setText(String.valueOf(position + 1));
        if (groupList.get(position) != null) {
            GroupResult group = groupList.get(position);
            Glide.with(Objects.requireNonNull(context))
                    .load(group.getAvatarUrl())
                    .into(holder.avatar);
            holder.groupId.setText(String.valueOf(group.getGroupId()));
            holder.companyName.setText(group.getCompanyName());
            holder.score.setText(String.valueOf((group.getTasteScoreF() + group.getTasteScoreM()) / 2.0));
        }
    }

}
