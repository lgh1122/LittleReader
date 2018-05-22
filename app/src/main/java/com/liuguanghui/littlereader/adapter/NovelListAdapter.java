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
import com.liuguanghui.littlereader.util.ImageLoader;

import java.util.List;

/**
 * Created by liuguanghui on 2018/5/10.
 */

public class NovelListAdapter  extends BaseAdapter {
    private Context context;
    //布局加载器
    private LayoutInflater mInflater;
    private List<NovelVO> novelInfos;
    private ImageLoader imageLoader;
    //动态改变appInfos
    public void setAppInfos(List<NovelVO> novelInfos) {
        this.novelInfos = novelInfos;
    }

    public NovelListAdapter(Context context, List<NovelVO> appInfos, ImageLoader imageLoader) {
        this.context = context;
        this.novelInfos = appInfos;
        this.imageLoader = imageLoader;
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

            convertView = mInflater.inflate(R.layout.list_item_view, null);
        }

        //获取布局控件
        //得到子View对象
        ImageView imageView = (ImageView) convertView.findViewById(R.id.detail_content_image);
        TextView titleTV = (TextView) convertView.findViewById(R.id.main_list_title);
        TextView chapterTV = (TextView) convertView.findViewById(R.id.main_list_chapter);
        TextView main_list_novelid = (TextView) convertView.findViewById(R.id.main_list_novelid);
        TextView main_list_netid = (TextView) convertView.findViewById(R.id.main_list_netid);

        //获取position位置上的AppInfo对象
        NovelVO novelInfo = novelInfos.get(position);
        if(novelInfo.getImgpath() != null && !"".equals(novelInfo.getImgpath())){
            imageLoader.loadImage(novelInfo.getImgpath(),imageView) ;
        }else{
            imageView.setImageResource(R.mipmap.commonnovelimg);
        }


        titleTV.setText(novelInfo.getTitle());
        chapterTV.setText(novelInfo.getLatestchaptername());
        main_list_novelid.setText(novelInfo.getId()+"");
        main_list_netid.setText(novelInfo.getNetid() +"");
        return convertView;
    }
}
