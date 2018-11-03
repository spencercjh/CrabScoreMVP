package top.spencer.crabscore.view.fragment.judge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import top.spencer.crabscore.R;

import java.util.Objects;

/**
 * 评委用户组的所有小组页面
 *
 * @author spencercjh
 */
public class AllGroupFragment extends Fragment {
    private View view;

    public static AllGroupFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        AllGroupFragment fragment = new AllGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sub_fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv = view.findViewById(R.id.fragment_test_tv);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = Objects.requireNonNull(bundle.get("name")).toString();
            tv.setText(name);
        }
    }

    //TODO AllGroupFragment
}