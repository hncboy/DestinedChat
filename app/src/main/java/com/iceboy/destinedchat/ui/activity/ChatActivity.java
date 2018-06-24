package com.iceboy.destinedchat.ui.activity;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.EMMessageListenerAdapter;
import com.iceboy.destinedchat.adapter.MessageListAdapter;
import com.iceboy.destinedchat.adapter.TextWatcherAdapter;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.presenter.ChatPresenter;
import com.iceboy.destinedchat.presenter.impl.ChatPresenterImpl;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.iceboy.destinedchat.view.ChatView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements ChatView {

    private static final String TAG = "ChatActivity";

    private static final int VIDEOCALL = 1;
    private static final int VOICECALL = 2;

    private ChatPresenter mChatPresenter;
    private String mUsername;
    private MessageListAdapter mMessageListAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.toolbar_function1)
    ImageView mBack;

    @BindView(R.id.toolbar_function2)
    ImageView mMoreFunction;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.edit)
    EditText mEdit;

    @BindView(R.id.send)
    Button mSend;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    protected void init() {
        //当点击通知进入聊天界面时关闭通知
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancel(1);
        mChatPresenter = new ChatPresenterImpl(this);
        initToolbar();
        mEdit.setOnEditorActionListener(mOnEditorActionListener);
        mEdit.addTextChangedListener(mTextWatcher);
        initRecyclerView();
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
        mChatPresenter.loadMessages(mUsername);
    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageListAdapter = new MessageListAdapter(this, mChatPresenter.getMessages());
        mRecyclerView.setAdapter(mMessageListAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void initToolbar() {
        mUsername = getIntent().getStringExtra(Constant.Extra.USERNAME);
        String title = String.format(getString(R.string.chat_with), mUsername);
        mTitle.setText(title);
        mBack.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp));
    }

    @OnClick({R.id.toolbar_function1, R.id.send, R.id.toolbar_function2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_function1:
                finish();
                break;
            case R.id.send:
                sendMessage();
                break;
            case R.id.toolbar_function2:
                setMoreFunction();
                break;
        }
    }

    /**
     * * 设置右上角的dialog
     */
    private void setMoreFunction() {
        String[] items = {getString(R.string.voice_call), getString(R.string.video_call)};
        AlertDialog alertDialog = new AlertDialog
                .Builder(this)
                .setItems(items, mOnClickListener)
                .show();
        //show后设置dialog的大小和位置
        Window dialogWindow = alertDialog.getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = 500;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.x = 500;
        params.y = -550;
        dialogWindow.setAttributes(params);
    }

    /**
     * dialog的点击事件
     */
    private DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    initiateCallObject(VOICECALL);
                    break;
                case 1:
                    initiateCallObject(VIDEOCALL);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 初始化语音聊天和视频通话对象
     *
     * @param type
     */
    private void initiateCallObject(int type) {
        if (type == VIDEOCALL) {
            //视频通话
            toast(getString(R.string.video_call));
        } else {
            //语音呼叫
            toast(getString(R.string.voice_call));
        }
    }

    @Override
    public void onStartSendMessage() {
        updateList();
    }

    @Override
    public void onSendMessageSuccess() {
        hideProgress();
        updateList();
    }

    @Override
    public void onSendMessageFailed() {
        hideProgress();
        toast(getString(R.string.send_failed));
    }

    @Override
    public void onMessagesLoaded() {
        mMessageListAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    @Override
    public void onMoreMessagesLoaded(int size) {
        mMessageListAdapter.notifyDataSetChanged();
        //滑动到加载了最新消息的地方
        mRecyclerView.scrollToPosition(size);
    }

    @Override
    public void onNoMoreData() {
        toast(getString(R.string.no_more_data));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }

    /**
     * 更新消息列表
     */
    private void updateList() {
        mMessageListAdapter.notifyDataSetChanged();
        smoothScrollToBottom();
    }

    /**
     * 将显示的最新的消息移动到底部
     */
    private void scrollToBottom() {
        mRecyclerView.scrollToPosition(mMessageListAdapter.getItemCount() - 1);
    }

    /**
     * 平滑移动到底部显示刚发的信息
     */
    private void smoothScrollToBottom() {
        mRecyclerView.smoothScrollToPosition(mMessageListAdapter.getItemCount() - 1);
    }

    /**
     * 发送消息
     */
    private void sendMessage() {
        mChatPresenter.sendMessage(mUsername, mEdit.getText().toString());
        hideKeyBoard();
        mEdit.getText().clear();
    }

    /**
     * 监听RecyclerView的滑动
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //状态为停止滑动
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //获取第一个可见view的位置
                int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                //如果position为0则加载该用户的更多聊天记录
                if (firstVisibleItemPosition == 0) {
                    mChatPresenter.loadMoreMessages(mUsername);
                }
            }
        }
    };

    /**
     * 注册消息的监听
     */
    private EMMessageListenerAdapter mEMMessageListener = new EMMessageListenerAdapter() {

        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EMMessage emMessage = list.get(0);
                    //如果消息对象一样，则显示已读，增加一条消息并滑动到底部
                    if (emMessage.getUserName().equals(mUsername)) {
                        mChatPresenter.makeMessageRead(mUsername);
                        mMessageListAdapter.addNewMessage(emMessage);
                        smoothScrollToBottom();
                    }
                }
            });
        }
    };

    /**
     * 软键盘点击发送的监听
     */
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        }
    };

    /**
     * 输入框消息的监听
     */
    private TextWatcherAdapter mTextWatcher = new TextWatcherAdapter() {

        @Override
        public void afterTextChanged(Editable s) {
            //当输入框中无消息时不能点发送
            mSend.setEnabled(s.length() != 0);
        }
    };
}
