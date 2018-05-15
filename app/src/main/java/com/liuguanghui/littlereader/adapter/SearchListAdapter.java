package com.liuguanghui.littlereader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuguanghui.littlereader.R;
import com.liuguanghui.littlereader.pojo.NovelVO;

import java.util.List;

/**
 * Created by liuguanghui on 2018/5/10.
 */

public class SearchListAdapter extends BaseAdapter {
    private Context context;
    //布局加载器
    private LayoutInflater mInflater;
    private List<NovelVO> novelInfos;

    //动态改变appInfos
    public void setAppInfos(List<NovelVO> novelInfos) {
        this.novelInfos = novelInfos;
    }

    public SearchListAdapter(Context context, List<NovelVO> appInfos) {
        this.context = context;
        this.novelInfos = appInfos;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return novelInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return novelInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1 得到控件
        //2 得到数据
        //3 绑定数据

        if(convertView == null){

            convertView = mInflater.inflate(R.layout.list_search_novel_view, null);
        }

        //获取布局控件
        //得到子View对象
        ImageView imageView = (ImageView) convertView.findViewById(R.id.search_image);
        TextView nameTV = (TextView) convertView.findViewById(R.id.search_novel_title);
        TextView typeTV = (TextView) convertView.findViewById(R.id.search_novel_type);
        TextView authorTV = (TextView) convertView.findViewById(R.id.search_novel_author);

        //获取position位置上的AppInfo对象
        NovelVO novelVO = novelInfos.get(position);

        imageView.setImageResource(R.mipmap.commonnovelimg);
        nameTV.setText(novelVO.getTitle());
        typeTV.setText(novelVO.getTname());
        authorTV.setText(novelVO.getAuthor()+"著");
        return convertView;
    }
}
