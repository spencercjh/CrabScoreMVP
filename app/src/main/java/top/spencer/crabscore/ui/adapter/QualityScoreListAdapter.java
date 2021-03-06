package top.spencer.crabscore.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import top.spencer.crabscore.R;
import top.spencer.crabscore.common.CommonConstant;
import top.spencer.crabscore.model.entity.QualityScore;

import java.util.List;

/**
 * @author spencercjh
 */
public class QualityScoreListAdapter extends RecyclerView.Adapter<QualityScoreListViewHolder> {
    private MyOnItemClickListener myOnItemClickListener;
    private List<QualityScore> qualityScoreList;

    public QualityScoreListAdapter(List<QualityScore> qualityScoreList) {
        this.qualityScoreList = qualityScoreList;
    }

    public void setOnItemClickListener(MyOnItemClickListener mListener) {
        this.myOnItemClickListener = mListener;
    }

    @SuppressWarnings("Duplicates")
    @NonNull
    @Override
    public QualityScoreListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quality_score, parent, false);
        QualityScoreListViewHolder qualityScoreListViewHolder = new QualityScoreListViewHolder(v);
        if (myOnItemClickListener != null) {
            qualityScoreListViewHolder.itemView.setOnClickListener(v1 -> myOnItemClickListener.onItemClick(v1));
            qualityScoreListViewHolder.itemView.setOnLongClickListener(v12 -> {
                myOnItemClickListener.onItemLongClick(v12);
                return true;
            });
        }
        return qualityScoreListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QualityScoreListViewHolder holder, int position) {
        if (qualityScoreList.get(position) != null) {
            QualityScore qualityScore = qualityScoreList.get(position);
            try {
                holder.itemView.setTag(qualityScore);
                holder.crabId.setText(String.valueOf(qualityScore.getCrabId()));
                holder.crabSex.setText((qualityScore.getCrabSex().equals(CommonConstant.CRAB_MALE) ? "雄性" : "雌性"));
                holder.userId.setText(String.valueOf(qualityScore.getUserId()));
                holder.scoreFin.setText(String.valueOf(qualityScore.getScoreFin()));
                holder.scoreBts.setText(String.valueOf(qualityScore.getScoreBts()));
                holder.scoreFts.setText(String.valueOf(qualityScore.getScoreFts()));
                holder.scoreEc.setText(String.valueOf(qualityScore.getScoreEc()));
                holder.scoreDscc.setText(String.valueOf(qualityScore.getScoreDscc()));
                holder.scoreBbyzt.setText(String.valueOf(qualityScore.getScoreBbyzt()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return qualityScoreList.size();
    }
}
