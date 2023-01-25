package app.myproject.yujincoffee_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.myproject.yujincoffee_app.Model.Product.ProductModel;
import app.myproject.yujincoffee_app.R;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    ArrayList<ProductModel> item;

    public MyOrderAdapter(ArrayList<ProductModel> item) {
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(item.get(position).getTem()>0){
            holder.no.setText(Integer.toString(position+1));
            holder.shopName.setText(item.get(position).getName());
            holder.shopAmount.setText(Integer.toString(item.get(position).getAmount()));
            holder.shopDollar.setText(Integer.toString(item.get(position).getDollar() ));
            holder.shopSugar.setText(item.get(position).getSugar());
            holder.shopIce.setText(item.get(position).getIce());
        }else{
            holder.no.setText(Integer.toString(position));
            holder.shopName.setText(item.get(position).getName());
            holder.shopAmount.setText(Integer.toString(item.get(position).getAmount()));
            holder.shopDollar.setText(Integer.toString(item.get(position).getDollar()));
            holder.shopSugar.setVisibility(View.INVISIBLE);
            holder.shopIce.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
       private TextView no;
       private TextView shopName;
       private TextView shopSugar;
       private TextView shopIce;
       private TextView shopAmount;
       private TextView shopDollar;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           this.no=itemView.findViewById(R.id.no);
           this.shopName=itemView.findViewById(R.id.shopName);
           this.shopSugar=itemView.findViewById(R.id.shopSugar);
           this.shopIce=itemView.findViewById(R.id.shopIce);
           this.shopAmount=itemView.findViewById(R.id.shopAmount);
           this.shopDollar=itemView.findViewById(R.id.shopDollar);

       }
   }
}
