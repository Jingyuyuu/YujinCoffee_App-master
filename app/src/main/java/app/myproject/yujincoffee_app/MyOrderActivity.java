package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import app.myproject.yujincoffee_app.Adapter.MyOrderAdapter;
import app.myproject.yujincoffee_app.Model.Product.ProductModel;
import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityMyOrderBinding;

public class MyOrderActivity extends AppCompatActivity {
    //點擊加入購物車按鈕後 將資料放入我的訂單頁面(MyOrderActivity)
    ActivityMyOrderBinding binding;
    SharedPreferences memberDataPre;
    ArrayList<ProductModel> item;
    MyOrderAdapter adapter;
    SQLiteDatabase db;
    private String createTable="create table if not exists tempProductOrder("+
            "_id integer"+" PRIMARY KEY AUTOINCREMENT,"+
            "shopName text,"+
            "shopTem integer,"+
            "shopSugar text,"+
            "shopIce text,"+
            "shopAmount integer,"+
            "shopPrice integer,"+
            "date text"+
            ");";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //打開資料庫查看有無tempProductOrder資料表
        db=openOrCreateDatabase("yujin",MODE_PRIVATE,null);
//        db.execSQL(createTable);
        item=new ArrayList<>();
        //先抓出tem>0的資料
        Cursor cursor=db.rawQuery("select * from tempProductOrder where shopTem>0;",null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                ProductModel a = new ProductModel(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getInt(6));
                item.add(a);
            }while(cursor.moveToNext());
        }
        //先抓出tem=0的資料
        Cursor cursor2=db.rawQuery("select * from tempProductOrder where shopTem==0;",null);
        if(cursor2.getCount()>0){
            cursor2.moveToFirst();
            do{
                ProductModel b = new ProductModel(cursor2.getInt(0)
                        ,cursor2.getString(1)
                        ,cursor2.getInt(2),
                        cursor2.getInt(5),
                        cursor2.getInt(6));
                item.add(b);
            }while(cursor.moveToNext());
        }
        adapter=new MyOrderAdapter(item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyOrderActivity.this);
        binding.shoppingCart.setLayoutManager(linearLayoutManager);
        //建立左滑能刪除的機制
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(binding.shoppingCart);
        binding.shoppingCart.setAdapter(adapter);
    }
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            item.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        //用id判斷點了哪個選項
        if(id == R.id.membersetting){
            Intent intent=new Intent(MyOrderActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(MyOrderActivity.this,MyOrderActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(MyOrderActivity.this, MenuListActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(MyOrderActivity.this,HistoryOrderActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myfavorite){
            Intent intent=new Intent(MyOrderActivity.this,MyFavoriteActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.storelists){
            Intent intent=new Intent(MyOrderActivity.this,storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(MyOrderActivity.this,PointChangeActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.logout){

            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(MyOrderActivity.this);
            logoutbtn.setTitle("登出");
            logoutbtn.setMessage("確定要登出嗎?");
            logoutbtn.setNegativeButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    memberDataPre= getSharedPreferences("memberDataPre", MODE_PRIVATE);
                    SharedPreferences.Editor editor=memberDataPre.edit();
                    editor.remove("name");
                    editor.remove("points");
                    editor.remove("phone");
                    editor.remove("email");
                    editor.apply();
                }
            });
            logoutbtn.setPositiveButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = logoutbtn.create();
            dialog.show();
        }
        else if(id ==android.R.id.home){
            //返回鍵動作
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}