///*
// * Copyright (C) 2014 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.android.im.imadapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.StaggeredGridLayoutManager;
//
//import com.android.im.R;
//import com.android.im.imbean.IMFindTotleData;
//import com.android.nettylibrary.utils.IMDensityUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class IMNewFindAdapter extends RecyclerView.Adapter<IMNewFindAdapter.MyViewHolder> {
//    private Context context;
//    private List<IMFindTotleData> datas = new ArrayList<>();
//
//    public IMNewFindAdapter(List<IMFindTotleData> datas, Context context) {
//        this.datas = datas;
//        this.context = context;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_im_find_item, parent, false));
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        IMFindTotleData bean = datas.get(position);
//        holder.textView.setText(bean.getTitle());
//
//
//        StaggeredGridAdapter staggeredGridAdapter = new StaggeredGridAdapter(context, bean.getDatas());
//        holder.recyclerView.setAdapter(staggeredGridAdapter);
//    }
//
//    @Override
//    public int getItemCount() {
//        return  datas.size();
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView textView;
//        public RecyclerView recyclerView;
//        public MyViewHolder(View view) {
//            super(view);
//            textView =  view.findViewById(R.id.tv_title);
//            recyclerView =  view.findViewById(R.id.recyclerView);
//            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
////            staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//            recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        }
//    }
//}