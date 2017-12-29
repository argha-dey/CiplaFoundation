package com.ciplafoundation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciplafoundation.R;
import com.ciplafoundation.model.PendingProposal;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by User-129-pc on 27-10-2017.
 */



public class AdapterRejectedProposalList extends RecyclerView.Adapter<AdapterRejectedProposalList.MyViewHolder>  {
    private Context mContext;
    private ArrayList<PendingProposal> rejectedProjectList=new ArrayList<>();
    private UserClass user=new UserClass();
    private VolleyTaskManager volleyTaskManager;
    private boolean isProposalDetails=false;
    private Prefs prefs;
    private RecyclerView rcl;
    public AdapterRejectedProposalList(Context _mContext, RecyclerView rcv) {
        this.mContext = _mContext;
        volleyTaskManager=new VolleyTaskManager(_mContext);
        this.rcl=rcv;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reject_proposal_list_single_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_header.setText(rejectedProjectList.get(position).getTitle());
        // holder.rl_child.setVisibility(View.GONE);
        holder.rl_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rcl.smoothScrollToPosition(position);
                if (holder.rl_child.isShown()) {
                    Util.collapse(holder.rl_child);
                    //holder.rl_child.setVisibility(View.GONE);
                    holder.iv_arrow.setBackgroundResource(R.drawable.accrodian_drop);

                } else {
                    //holder.rl_child.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.slide_down));
                    holder.iv_arrow.setBackgroundResource(R.drawable.accrodian_dropdown);
                    Util.expand(holder.rl_child);
                    //holder.rl_child.setVisibility(View.VISIBLE);

                    SpannableStringBuilder builder_timeline = new SpannableStringBuilder();
                    String timeline=rejectedProjectList.get(position).getTime_line_date();
                    builder_timeline.append(timeline);
                    String duration = " ( "+rejectedProjectList.get(position).getTime_line_duration()+" )";
                    SpannableString spannableString= new SpannableString(duration);
                    spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.login_btn_unclicked)), 0, spannableString.length(), 0);
                    builder_timeline.append(spannableString);
                    holder.tv_timeline_data_view.setText(builder_timeline, TextView.BufferType.SPANNABLE);

                    //holder.tv_budget_data_view.setText(pendingProposalList.get(position).getBudget());
                    /*holder.tv_budget_data_view.setText(
                            NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(pendingProposalList.get(position).getBudget())));*/
                /*    holder.tv_budget_data_view.setText(
                            String.format("%,d",Integer.parseInt(pendingProposalList.get(position).getBudget())));*/
                    if(!rejectedProjectList.get(position).getBudget().isEmpty())
                        holder.tv_budget_data_view.setText(new DecimalFormat("##,##,##0").format(Integer.parseInt(rejectedProjectList.get(position).getBudget())));

                    SpannableStringBuilder builder_createdBy = new SpannableStringBuilder();
                    String CreatedBy="By "+rejectedProjectList.get(position).getCreated_by()+" on " ;
                    builder_createdBy.append(CreatedBy);
                    String created_date=rejectedProjectList.get(position).getCreated_date();
                    SpannableString spannableString_date= new SpannableString(created_date);
                    spannableString_date.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.login_btn_unclicked)), 0, spannableString_date.length(), 0);
                    builder_createdBy.append(spannableString_date);
                    holder.tv_created_data_view.setText(builder_createdBy, TextView.BufferType.SPANNABLE);

                    String NGO_type=rejectedProjectList.get(position).getNgo_type();
                    if(NGO_type.equalsIgnoreCase("NGO"))
                    {
                        holder.tv_implemented_by_NGO_data_view.setText("NGO");
                        holder.tv_implemented_by_data_view.setText(rejectedProjectList.get(position).getNgo());
                        String str=rejectedProjectList.get(position).getNgo();
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
                        holder.tv_implemented_by_NGO_data_view.setText(rejectedProjectList.get(position).getNgo_type());
                        holder.tv_implemented_by_data_view.setVisibility(View.GONE);
                    }



                }
            }
        });


        holder.ib_btn_reject_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Util.checkConnectivity(mContext))
                {
                    user=Util.fetchUserClass(mContext);
                    String id=rejectedProjectList.get(position).getId();
                    String user_id=user.getUserId();
                    prefs=new Prefs(mContext);
                    prefs.setIsProposalDetails(true);
                    String division_id = prefs.getDivisionId();
                    String role_id=user.getRoleId();
                    String paramMap="USR_USER_ID="+user_id+"&ID="+id+"&DIVISION_ID="+division_id+"&ROLE_ID="+role_id;
                    //isProposalDetails=true;
                    volleyTaskManager.doGetProposalDetails(paramMap,true);
                }
                else
                    Util.showMessageWithOk(mContext,mContext.getString(R.string.no_internet));

            }
        });

    }

    @Override
    public int getItemCount() {
        return rejectedProjectList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_header,tv_timeline_data_view,tv_implemented_by_data_view,
                tv_budget_data_view,tv_created_data_view,tv_implemented_by_NGO_data_view;
        public RelativeLayout rl_header, rl_child;
        public ImageButton ib_btn_reject_details;
        public ImageView iv_arrow;


        public MyViewHolder(View itemView) {
            super(itemView);
            tv_header = (TextView) itemView.findViewById(R.id.tv_reject_header);
            rl_header = (RelativeLayout) itemView.findViewById(R.id.rl_reject_header);
            rl_child = (RelativeLayout) itemView.findViewById(R.id.rl_reject_child);
            tv_timeline_data_view = (TextView) itemView.findViewById(R.id.tv_timeline_data_view);
            tv_implemented_by_NGO_data_view = (TextView) itemView.findViewById(R.id.tv_implemented_by_NGO_data_view);
            tv_implemented_by_data_view = (TextView) itemView.findViewById(R.id.tv_implemented_by_data_view);
            tv_budget_data_view = (TextView) itemView.findViewById(R.id.tv_budget_data_view);
            tv_created_data_view = (TextView) itemView.findViewById(R.id.tv_created_data_view);
            ib_btn_reject_details = (ImageButton) itemView.findViewById(R.id.ib_btn_reject_details);
            iv_arrow = (ImageView) itemView.findViewById(R.id.iv_arrow_reject);

        }
    }

    public void AddArray(ArrayList<PendingProposal> _arrDataFormListItems) {
        rejectedProjectList.clear();
        if (_arrDataFormListItems != null
                && _arrDataFormListItems.size() > 0) {
            rejectedProjectList.addAll(_arrDataFormListItems);
        }
        notifyDataSetChanged();

    }
}
