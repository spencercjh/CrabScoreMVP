package top.spencer.crabscore.view.fragment.administrator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import com.alibaba.fastjson.JSONObject;
import top.spencer.crabscore.R;
import top.spencer.crabscore.base.BaseFragment;
import top.spencer.crabscore.common.CommonConstant;
import top.spencer.crabscore.model.entity.User;
import top.spencer.crabscore.presenter.AdministratorListPresenter;
import top.spencer.crabscore.util.PatternUtil;
import top.spencer.crabscore.util.SharedPreferencesUtil;
import top.spencer.crabscore.view.adapter.MyOnItemClickListener;
import top.spencer.crabscore.view.adapter.UserAdminListAdapter;
import top.spencer.crabscore.view.view.MyRecycleListView;
import top.spencer.crabscore.view.widget.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 管理员用户组的用户管理页面
 *
 * @author spencercjh
 */
public class UserAdminFragment extends BaseFragment implements MyRecycleListView {
    @BindView(R.id.recycler_view_list)
    EmptyRecyclerView userListView;
    @BindView(R.id.textview_empty)
    TextView emptyText;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private AdministratorListPresenter administratorListPresenter;
    private String jwt;
    private UserAdminListAdapter userAdminListAdapter;
    private List<User> userList = new ArrayList<>(10);
    private int pageNum = 1;
    private boolean repeat = false;

    /**
     * 取得实例
     *
     * @param name 测试参数
     * @return fragment
     */
    public static UserAdminFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        UserAdminFragment fragment = new UserAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 获得fragment的layout的Id
     *
     * @return layout Id
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_list;
    }

    /**
     * 重写onDestroyView 断开view
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        administratorListPresenter.detachView();
    }

    /**
     * 初始化视图
     *
     * @param view               view
     * @param savedInstanceState saveInstanceState
     */
    @SuppressWarnings("Duplicates")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        administratorListPresenter = new AdministratorListPresenter();
        administratorListPresenter.attachView(this);
        SharedPreferencesUtil.getInstance(getContext(), "PROPERTY");
        jwt = (String) (SharedPreferencesUtil.getData("JWT", ""));
        setRecycleView();
    }

    /**
     * 初始化RecycleView
     */
    @Override
    public void setRecycleView() {
        userAdminListAdapter = new UserAdminListAdapter(userList);
        userAdminListAdapter.setOnItemClickListener(new MyOnItemClickListener() {

            @Override
            public void onItemClick(View view) {
                final User user = (User) view.getTag();
                final PopupMenu popupMenu = new PopupMenu(getContext(), view);
                final MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.pop_menu_user_admin, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit_user_info: {
                                initEditUserInfoDialog(user);
                                popupMenu.dismiss();
                                break;
                            }
                            case R.id.menu_ban_user: {
                                break;
                            }
                            case R.id.menu_allow_all_competition: {
                                break;
                            }
                            case R.id.menu_allow_only_present_competition: {
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }

            @Override
            public void onItemLongClick(View view) {
            }
        });
        if (userList.size() == 0) {
            userListView.setEmptyView(emptyText);
        }
        userListView.setAdapter(userAdminListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                administratorListPresenter.getAllUser(pageNum, pageSize, jwt);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        userListView.setLayoutManager(layoutManager);
        userListView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()),
                DividerItemDecoration.VERTICAL));
        final int[] lastVisibleItemPosition = {0};
        userListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition[0] + 1 == userAdminListAdapter.getItemCount()) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (!repeat) {
                        administratorListPresenter.getAllUser(pageNum, pageSize, jwt);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition[0] = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 初始发起请求
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        administratorListPresenter.getAllUser(pageNum, pageSize, jwt);
    }

    /**
     * getAllUser请求成功
     *
     * @param successData 成功数据源
     */
    @Override
    public void showData(JSONObject successData) {
        pageNum++;
        repeat = administratorListPresenter.dealUserListJSON(successData.getJSONArray("result"), userList);
        if (repeat) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                userAdminListAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * updateUserProperty请求成功
     *
     * @param successData 成功数据源
     */
    @Override
    public void showResponse1(JSONObject successData) {
        if (successData.getInteger("code").equals(CommonConstant.SUCCESS)) {
            showToast(successData.getString("message"));
        }
    }

    /**
     * 编辑用户信息AlertDialog
     *
     * @param userInDialog 用户对象
     */
    private void initEditUserInfoDialog(final User userInDialog) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_admin_edit_user_info, null);
        final EditText username = dialogView.findViewById(R.id.edit_username);
        username.setText(userInDialog.getUserName());
        final EditText displayName = dialogView.findViewById(R.id.edit_display_name);
        displayName.setText(userInDialog.getDisplayName());
        final EditText phone = dialogView.findViewById(R.id.edit_phone);
        phone.setText(userInDialog.getEmail());
        final Spinner roleSpinner = dialogView.findViewById(R.id.spinner_role);
        initSpinner(roleSpinner, userInDialog);
        roleSpinner.setSelection(userInDialog.getRoleId());
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        Window dialogWindow = dialog.getWindow();
        Objects.requireNonNull(dialogWindow).setGravity(Gravity.CENTER);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setIcon(R.drawable.app_logo);
        dialog.setTitle("修改用户信息");
        dialog.setView(dialogView);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String usernameString = username.getText().toString().trim();
                String displayNameString = displayName.getText().toString().trim();
                String mobile = phone.getText().toString().trim();
                if (!PatternUtil.isUsername(usernameString)) {
                    showToast("非法用户名");
                    return;
                } else if (!PatternUtil.isName(displayNameString)) {
                    showToast("非法显示名");
                    return;
                } else if (!PatternUtil.isMobile(mobile)) {
                    showToast("非法手机号");
                    return;
                } else {
                    userInDialog.setUserName(usernameString);
                    userInDialog.setDisplayName(displayNameString);
                    userInDialog.setEmail(mobile);
                    administratorListPresenter.updateUserProperty(userInDialog,
                            (String) SharedPreferencesUtil.getData("JWT", ""));
                }
                dialog.dismiss();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 初始化编辑用户信息AlertDialog里的Spinner
     *
     * @param roleSpinner  spinner
     * @param userInDialog user
     */
    private void initSpinner(Spinner roleSpinner, final User userInDialog) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.roles));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                userInDialog.setRoleId(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}