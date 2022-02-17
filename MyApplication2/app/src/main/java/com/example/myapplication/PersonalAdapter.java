package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class PersonalAdapter extends ArrayAdapter<String> {
        private ArrayList<String> arrayListArticle;

        public PersonalAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            arrayListArticle = (ArrayList<String>) objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.asfsd, parent, false);
                viewHolder.discover_username = convertView.findViewById(R.id.safsa);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position < arrayListArticle.size()) {
                final String article = getItem(position);//recyclerViewActivity
                viewHolder.discover_username.setText("item:"+article);//设置用户名
//                Glide.with(recyclerViewActivity).load(article.getHeadImg()).centerCrop().into(viewHolder.discover_image);//设置头像
//                viewHolder.discover_username.setText(article.getNickname());//设置用户名
//                viewHolder.discover_time.setText(article.getCreate_time());//设置发布时间
//                viewHolder.discover_give_text.setText(article.getGive_number());//设置点赞总数
//                viewHolder.discover_comment_text.setText(article.getComment_number());//设置评论总数
//                if (!("".equals(article.getContent()))) {//判断是否有文本信息
//                    viewHolder.discover_text.setText(article.getContent());
//                    viewHolder.discover_text.setVisibility(View.VISIBLE);
//                }
//
//                if ("like".equals(article.getGive_status())) {//已点赞
//                    viewHolder.discover_give.setBackgroundResource(R.mipmap.discover_give_yes);
//                } else if ("dislike".equals(article.getGive_status())) {//未点赞
//                    viewHolder.discover_give.setBackgroundResource(R.mipmap.discover_give_not);
//                }
//
//                if ("0".equals(article.getBuddy_status())) {//不是好友
//                    viewHolder.discover_whether_friend.setVisibility(View.VISIBLE);
//                    if (article.getUser_id().equals(CommUtil.user_id)) {
//                        viewHolder.discover_whether_friend.setVisibility(View.GONE);
//                    }
//                } else if ("1".equals(article.getBuddy_status())) {
//                    viewHolder.discover_whether_friend.setVisibility(View.GONE);
//                    viewHolder.discover_whether_friend.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //进入个人信息页面
////                            Intent intent = new Intent(personalInformation, PersonalInformation.class);
////                            intent.putExtra("to_uid", article.getUser_id());
////                            personalInformation.startActivity(intent);
//                        }
//                    });
//                }
//
//                //点击了评论(显示评论控件)
//                viewHolder.discover_comment_image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                    linearLayout.setVisibility(View.VISIBLE);
////                    PersonalFragment.circle_id = article.getCircle_id();
////                    PersonalFragment.personal_comment_text = viewHolder.discover_comment_text;
//                        // CommUtil.showInput(PersonalFragment.personal_friends_edit);
//                        final View commentView = LayoutInflater.from(recyclerViewActivity).inflate(R.layout.activity_comment, null, false);
//                        final PopupWindow sharePop = new PopupWindow(commentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//                        sharePop.setBackgroundDrawable(null);
//                        sharePop.setFocusable(true);
//                        sharePop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//                        sharePop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                        sharePop.showAtLocation(recyclerViewActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//                        final EditText comment_edit = commentView.findViewById(R.id.comment_edit);
//                        Button comment_send_btn = commentView.findViewById(R.id.comment_send_btn);
//                        comment_send_btn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v1) {
//                                //调用评论接口
//                                System.out.println("点击了发送按钮：" + comment_edit.getText().toString().trim());
//                                String edit = comment_edit.getText().toString().trim();
//                                if ("".equals(edit)) {
//                                    Toast.makeText(homeActivity, "评论内容不能为空！", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    //此处to_uid给空是因为当前页面评论都是直接评论动态
//                                    boolean comment = Interface.postComment(article.getCircle_id(), "", edit);
//                                    if (comment) {
//                                        Toast.makeText(homeActivity, "评论成功！", Toast.LENGTH_SHORT).show();
//                                        //评论成功后需要刷新数据
//                                        int com = Integer.parseInt(viewHolder.discover_comment_text.getText().toString());
//                                        viewHolder.discover_comment_text.setText(String.valueOf(++com));
//                                        comment_edit.setText("");
//                                        sharePop.dismiss();
//                                        PersonalAdapter.this.hideInput();//关闭软键盘
//                                        for (int i = 0; i < PersonalFragment.personal_list.size(); i++) {//修改动态集合里对应的评论条数
//                                            if (article.getCircle_id().equals(PersonalFragment.personal_list.get(i).getCircle_id())) {
//                                                PersonalFragment.personal_list.get(i).setComment_number(String.valueOf(com));
//                                            }
//                                            ;
//                                        }
//                                    } else {
//                                        Toast.makeText(homeActivity, "评论失败，请联系管理员！", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        });
//                    }
//                });
//                //点击更多
//                viewHolder.discover_more.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PersonalFragment.circle_id = article.getCircle_id();
//                        final View commentView = LayoutInflater.from(recyclerViewActivity).inflate(R.layout.discovwe_more, null, false);
//                        final PopupWindow sharePop = new PopupWindow(commentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//                        sharePop.setBackgroundDrawable(null);
//                        sharePop.setFocusable(true);
//                        sharePop.showAtLocation(recyclerViewActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//                        TextView discover_more_top = commentView.findViewById(R.id.discover_more_top);
//                        TextView discover_more_bot = commentView.findViewById(R.id.discover_more_bot);
//                        if (article.getUser_id().equals(CommUtil.user_id)) {
//                            discover_more_top.setText("删除");
//                        } else {
//                            discover_more_top.setText("举报");
//                        }
//
//                        //点击了删除或者举报
//                        discover_more_top.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v1) {
//                                if (article.getUser_id().equals(CommUtil.user_id)) {
//                                    System.out.println("调用删除动态接口");
//                                    boolean deleteDynamic = Interface.deleteDynamic(article.getCircle_id());
//                                    if (deleteDynamic) {
//                                        Toast.makeText(recyclerViewActivity, "删除成功！", Toast.LENGTH_SHORT).show();
//                                        PersonalFragment.personal_list.remove(position);//删除list中对应的数据
//                                        PersonalFragment.getPersonal();//重新渲染
//                                        if (position > 1) {
//                                            PersonalFragment.personal_friends_list.setSelection(position - 1);//设置显示位置
//                                        }
//
//                                        sharePop.dismiss();//关闭弹窗
//                                    }
//                                } else {
//                                    if (RecyclerViewActivity.present.equals(CommUtil.now)) {
//                                        System.out.println("拉起举报页面");
//                                        sharePop.dismiss();//关闭弹窗
//                                        //待完成
//                                        Intent intent = new Intent();
//                                        intent.putExtra("circle_id", article.getCircle_id());
//                                        intent.putExtra("to_uid", article.getUser_id());
//                                        intent.setClass(recyclerViewActivity, DiscoverReport.class);
//                                        DiscoverReport.previous= RecyclerViewActivity.present;
//                                        recyclerViewActivity.startActivity(intent);
//                                    }
//
//                                }
//                            }
//                        });
//                        discover_more_bot.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v2) {
//                                sharePop.dismiss();
//                            }
//                        });
//                    }
//                });
//                //渲染图片数据
//                ArrayList<Image> list = new ArrayList<>();
//                String s = article.getPicture_video();
//                if ("[\"\"]".equals(s)) {
//                    viewHolder.discover_grid.setVisibility(View.GONE);
//                } else {
//                    JSONArray jsonArray = JSONArray.parseArray(s);
//                    for (int i = 0; i < jsonArray.size(); i++) {
//                        if ("".equals(jsonArray.get(i).toString())) {
//                            viewHolder.discover_grid.setVisibility(View.GONE);
//                        }
//                        list.add(new Image(jsonArray.get(i).toString()));
//                    }
//                    viewHolder.discover_grid.setAdapter(null);
//                    ImageAdapter adapter = new ImageAdapter(recyclerViewActivity, R.layout.discover_give_image, list);
//                    viewHolder.discover_grid.setAdapter(adapter);
//                    viewHolder.discover_grid.setVisibility(View.VISIBLE);
//                }
//
//
//                //点赞
//                viewHolder.discover_give.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        System.out.println("点赞");
//                        String give = Interface.circleGive(article.getUser_id(), article.getCircle_id());
//                        Integer number = Integer.parseInt(article.getGive_number());
//                        if ("点赞成功".equals(give)) {
//                            number++;
//                            article.setGive_number(String.valueOf(number));
//                            article.setGive_status("like");
//                            viewHolder.discover_give_text.setText(String.valueOf(number));
//                            viewHolder.discover_give.setBackgroundResource(R.mipmap.discover_give_yes);
//                        } else if ("取消点赞成功".equals(give)) {
//                            number--;
//                            article.setGive_number(String.valueOf(number));
//                            viewHolder.discover_give_text.setText(String.valueOf(number));
//                            article.setGive_status("dislike");
//                            viewHolder.discover_give.setBackgroundResource(R.mipmap.discover_give_not);
//                        }
//                        Toast.makeText(recyclerViewActivity, give, Toast.LENGTH_SHORT).show();
//                    }
//                });
//                //需要进入第二个页面
//                viewHolder.discover_all_content.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v1) {
//                        System.out.println("需要进入第二给页面" + article.getCircle_id());
//                        PersonalAdapter.this.toPart(article);
//                    }
//                });
//                viewHolder.discover_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        System.out.println("需要进入第二给页面1" + article.getCircle_id());
//                        toPart(article);
//                    }
//                });
         }
//            //setViewIn(convertView);
//            //动态设置GridView高度
//            setListViewHeightBasedOnChildren(viewHolder.discover_grid);
            return convertView;
        }

        private class ViewHolder {
            public TextView discover_username;//姓名
//            public ImageView discover_image;//头像
//            public TextView discover_time;//发表时间
//            public ImageView discover_whether_friend;//是否是好友
//            public FrameLayout discover_more;//更多(三个点)
//            public TextView discover_text;//发表的文本内容
//            public GridView discover_grid;//图片
//            public ImageView discover_give;//是否点赞
//            public TextView discover_give_text;//点赞数量
//            public TextView discover_comment_text;//评论数量
//            public ImageView discover_comment_image;//评论
//            public LinearLayout discover_all_content;//是否是好友
        }

//        private void toPart(Discover discover) {
//            if (HomeActivity.present.equals(CommUtil.now)) {
//                Intent intent = new Intent(recyclerViewActivity, DiscoverItemPart.class);
//                DiscoverItemPart.previous= RecyclerViewActivity.present;
//                recyclerViewActivity.startActivity(intent);
//                DiscoverItemPart.discover = discover;
//            }
//        }

        //动态设置GridView高度
        public static void setListViewHeightBasedOnChildren(GridView listView) {
            // 获取listview的adapter
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            // 固定列宽，有多少列
            int col = 4;// listView.getNumColumns();
            int totalHeight = 0;
            // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
            // listAdapter.getCount()小于等于8时计算两次高度相加
            for (int i = 0; i < listAdapter.getCount(); i += col) {
                // 获取listview的每一个item
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                // 获取item的高度和
                totalHeight += listItem.getMeasuredHeight();
            }

            // 获取listview的布局参数
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            // 设置高度
            params.height = totalHeight;
            // 设置margin
            ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
            // 设置参数
            listView.setLayoutParams(params);
        }
    /**
     * 隐藏键盘
     */
//        public  void hideInput() {
//            InputMethodManager imm = (InputMethodManager) recyclerViewActivity.getSystemService(INPUT_METHOD_SERVICE);
//            View v = recyclerViewActivity.getWindow().peekDecorView();
//            if (null != v) {
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            }
//        }
    }
