package com.ciplafoundation.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciplafoundation.R;
import com.ciplafoundation.model.AcceptedProposal;
import com.ciplafoundation.model.AccordianClass;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.Util;


import java.text.DecimalFormat;
import java.util.ArrayList;


public class AdapterApprovedProposalList extends RecyclerView.Adapter<AdapterApprovedProposalList.MyViewHolder> {
    private Context mContext;
    private ArrayList<AcceptedProposal> approvedProposalList=new ArrayList<>();
    private ArrayList<AccordianClass> accordianClassList=new ArrayList<>();
    private UserClass user=new UserClass();
    private VolleyTaskManager volleyTaskManager;
    private Prefs prefs;

    public AdapterApprovedProposalList(Context _mContext) {
        this.mContext = _mContext;
        volleyTaskManager=new VolleyTaskManager(_mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.approved_proposal_list_single_row, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
       /* return new MyViewHolder(itemView);*/
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_header.setText(approvedProposalList.get(position).getTitle());
        // holder.rl_child.setVisibility(View.GONE);


        holder.rl_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(approvedProposalList.get(position).getisExpanded())
                {
                    approvedProposalList.get(position).setisExpanded(false);
                    Util.collapse(holder.rl_child);
                    holder.iv_arrow_approved.setBackgroundResource(R.drawable.accrodian_drop);
                }
                else
                {
                    for(int i=0;i<approvedProposalList.size();i++)
                    {
                        if(approvedProposalList.get(i).getisExpanded())
                        {
                            approvedProposalList.get(i).setisExpanded(false);
                            //rl_child.setVisibility(View.GONE);
                            //holder.iv_arrow_approved.setBackgroundResource(R.drawable.accrodian_drop);
                        }
                    }
                    approvedProposalList.get(position).setisExpanded(true);


                    holder.iv_arrow_approved.setBackgroundResource(R.drawable.accrodian_dropdown);
                    Util.expand(holder.rl_child);
                    //holder.rl_child.setVisibility(View.VISIBLE);





               /* if (holder.rl_child.isShown()) {
                    //holder.rl_child.setVisibility(View.GONE);
                    //holder.iv_arrow_approved.setBackgroundResource(R.drawable.accrodian_drop);


                } else {*/
                    //Toast.makeText(mContext,""+holder.rl_child.getId(),Toast.LENGTH_SHORT).show();
                   // holder.rl_child.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.slide_down));
                   // holder.iv_arrow_approved.setBackgroundResource(R.drawable.accrodian_dropdown);
                   // holder.rl_child.setVisibility(View.VISIBLE);

                    SpannableStringBuilder builder_timeline = new SpannableStringBuilder();
                    String timeline=approvedProposalList.get(position).getTime_line_date();
                    builder_timeline.append(timeline);
                    String duration = " ( "+approvedProposalList.get(position).getTime_line_duration()+" )";
                    SpannableString spannableString= new SpannableString(duration);
                    spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.login_btn_unclicked)), 0, spannableString.length(), 0);
                    builder_timeline.append(spannableString);
                    holder.tv_timeline_data_view.setText(builder_timeline, TextView.BufferType.SPANNABLE);
                    holder.tv_approved_proposal_no_data_view.setText(approvedProposalList.get(position).getProposal_no());
                    //holder.tv_approved_proposal_no_data_view.setText("abcdef");

                    //holder.tv_budget_data_view.setText(pendingProposalList.get(position).getBudget());
                    /*holder.tv_budget_data_view.setText(
                            NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(pendingProposalList.get(position).getBudget())));*/
                    if(!approvedProposalList.get(position).getBudget().isEmpty())
                    holder.tv_budget_data_view.setText(
                            new DecimalFormat("##,##,##0").format(Integer.parseInt(approvedProposalList.get(position).getBudget())));

                    SpannableStringBuilder builder_createdBy = new SpannableStringBuilder();
                    String CreatedBy="By "+approvedProposalList.get(position).getCreated_by()+" on " ;
                    builder_createdBy.append(CreatedBy);
                    String created_date=approvedProposalList.get(position).getCreated_date();
                    SpannableString spannableString_date= new SpannableString(created_date);
                    spannableString_date.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.login_btn_unclicked)), 0, spannableString_date.length(), 0);
                    builder_createdBy.append(spannableString_date);
                    holder.tv_created_data_view.setText(builder_createdBy, TextView.BufferType.SPANNABLE);

                    String NGO_type=approvedProposalList.get(position).getNgo_type();
                    if(NGO_type.equalsIgnoreCase("NGO"))
                    {
                        holder.tv_implemented_by_NGO_data_view.setText("NGO");
                        holder.tv_implemented_by_data_view.setText(approvedProposalList.get(position).getNgo());
                        String str=approvedProposalList.get(position).getNgo();
                        int index=str.indexOf('|');
                        String first_part=str.substring(0,index-1)+" ";
                        String second_part=str.substring(index+1,str.length());
                        SpannableStringBuilder builder_implementBy = new SpannableStringBuilder();
                        builder_implementBy.append(first_part);
                        SpannableString spannableimp= new SpannableString(second_part);
                        spannableimp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.login_btn_unclicked)), 0, spannableimp.length(), 0);
                        builder_implementBy.append(spannableimp);
                        holder.tv_implemented_by_data_view.setText(builder_implementBy, TextView.BufferType.SPANNABLE);

                    }

                    else
                    {
                        holder.tv_implemented_by_NGO_data_view.setText(approvedProposalList.get(position).getNgo_type());
                        holder.tv_implemented_by_data_view.setVisibility(View.GONE);
                    }



                }
            }
        });

        holder.iv_btn_approved_deatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Util.checkConnectivity(mContext))
                {
                    user=Util.fetchUserClass(mContext);
                    String id=approvedProposalList.get(position).getId();
                    String user_id=user.getUserId();
                    String params="USR_USER_ID="+user_id+"&ID="+id;
                    prefs=new Prefs(mContext);
                    prefs.setIsProjectDetails(true);
                    //isProposalDetails=true;
                    volleyTaskManager.doGetProposalDetails(params,true);
                }
                else
                    Util.showMessageWithOk(mContext,mContext.getString(R.string.no_internet));
            }
        });
    }

    @Override
    public int getItemCount() {
        return approvedProposalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_header,tv_timeline_data_view,tv_implemented_by_data_view, tv_budget_data_view,
                tv_created_data_view,tv_implemented_by_NGO_data_view,tv_approved_proposal_no_data_view;
        public RelativeLayout rl_header, rl_child;
        public ImageButton iv_btn_approved_deatils;
        public ImageView iv_arrow_approved;


        public MyViewHolder(View itemView) {
            super(itemView);
            tv_header = (TextView) itemView.findViewById(R.id.tv_approved_header);
            rl_header = (RelativeLayout) itemView.findViewById(R.id.rl_approved_header);
            rl_child = (RelativeLayout) itemView.findViewById(R.id.rl_approved_child);
            tv_timeline_data_view = (TextView) itemView.findViewById(R.id.tv_approved_timeline_data_view);
            tv_approved_proposal_no_data_view= (TextView) itemView.findViewById(R.id.tv_approved_proposal_no_data_view);
            tv_implemented_by_NGO_data_view = (TextView) itemView.findViewById(R.id.tv_approved_implemented_by_NGO_data_view);
            tv_implemented_by_data_view = (TextView) itemView.findViewById(R.id.tv_approved_implemented_by_data_view);
            tv_budget_data_view = (TextView) itemView.findViewById(R.id.tv_approved_budget_data_view);
            tv_created_data_view = (TextView) itemView.findViewById(R.id.tv_approved_created_data_view);
            iv_btn_approved_deatils = (ImageButton) itemView.findViewById(R.id.iv_btn_approved_deatils);
            iv_arrow_approved = (ImageView) itemView.findViewById(R.id.iv_arrow_approved);

        }
    }

        public void AddArray(ArrayList<AcceptedProposal> _arrDataFormListItems) {
            approvedProposalList.clear();
            if (_arrDataFormListItems != null
                    && _arrDataFormListItems.size() > 0) {
                approvedProposalList.addAll(_arrDataFormListItems);
            }
            notifyDataSetChanged();

        }
}
