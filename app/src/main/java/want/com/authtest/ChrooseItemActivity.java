package want.com.authtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import want.com.authtest.bean.Tag;

public class ChrooseItemActivity extends Activity {
    public static final String ChrooseTypeIsTAG="ChrooseTypeIsTAG";
    private ListView mListView;
    private Handler mHandler;
    private MyAdapter mMyAdapter;
    private List<Tag> data_list=new ArrayList<>();
    private List<String> chrooseResult=new ArrayList<>();
    private TextView mMResult,checkAll;
    private boolean mIsTAG;
    private boolean isCheckAll=false;
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra(MainActivity.Result,mMResult.getText().toString());
        if(mIsTAG){
            setResult(MainActivity.GETTAG,intent);
        }else{
            setResult(MainActivity.GETPREM,intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chroose_item);
        mListView = (ListView) findViewById(R.id.listView);
        mMResult = (TextView) findViewById(R.id.result);
        checkAll = (TextView) findViewById(R.id.checkAll);
        mMyAdapter = new MyAdapter();
        mListView.setAdapter(mMyAdapter);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mMyAdapter.notifyDataSetChanged();
                updateResult();
            }
        };
        mIsTAG = this.getIntent().getBooleanExtra(ChrooseTypeIsTAG, false);
        initData();
        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isCheckAll=!isCheckAll;
                        for(int i=0;i<data_list.size();i++){
                            data_list.get(i).isCheck=isCheckAll;
                            if(isCheckAll){
                                chrooseResult.add(data_list.get(i).Name);
                            }else{
                                chrooseResult.remove(data_list.get(i).Name);
                            }
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
    }
    public void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mIsTAG){
                    data_list = PermTagUtils.getTag();
                }else{
                    data_list = PermTagUtils.getPerm(ChrooseItemActivity.this);
                }
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    public void updateResult(){
        StringBuilder result=new StringBuilder();
        if(isCheckAll){
            checkAll.setText("UNCheckAll");
        }else{
            checkAll.setText("CheckAll");
        }
        Iterator<String> iterator = chrooseResult.iterator();
        if(mIsTAG){
            while(iterator.hasNext()){
                result.append(iterator.next()+":");
            }
            if(result.length()>1){
                result.delete(result.length()-1,result.length());
            }
        }else{
            result.append("default ");
            while(iterator.hasNext()){
                result.append(" "+iterator.next());
            }
        }
        mMResult.setText(result.toString());
    }
    class MyAdapter extends  BaseAdapter{
        @Override
        public int getCount() {
            return data_list.size();
        }
        @Override
        public Object getItem(int i) {
            return data_list.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view!=null){
                holder= (ViewHolder) view.getTag();
            }else{
                view=View.inflate(ChrooseItemActivity.this,R.layout.item_listview,null);
                 holder=new ViewHolder(view);
                view.setTag(holder);
            }
            final String name=data_list.get(i).Name;
            holder.mTextView.setText(name);
            holder.mCheckBox.setOnCheckedChangeListener(null);
             holder.mCheckBox.setChecked(data_list.get(i).isCheck);
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    data_list.get(i).isCheck=b;
                    if(b){
                        chrooseResult.add(name);
                    }else{
                        chrooseResult.remove(name);
                    }
                    updateResult();
                }
            });
            return view;
        }
    }
    class ViewHolder{
        public TextView mTextView;
        public CheckBox mCheckBox;

        public ViewHolder(View view){
            mTextView= (TextView) view.findViewById(R.id.textView);
            mCheckBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }
}
