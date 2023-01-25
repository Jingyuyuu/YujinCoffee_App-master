package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.myproject.yujincoffee_app.Modle.Util.SimpleeAPIWorker;
import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityMemberdataaPageBinding;
import app.myproject.yujincoffee_app.databinding.ActivityRegisttPageBinding;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class memberdataaPageActivity extends AppCompatActivity {
    SharedPreferences memberDataSharePre;
    ActivityMemberdataaPageBinding binding;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMemberdataaPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        executorService = Executors.newSingleThreadExecutor();
        //getSharedPreferences只是建立檔名 可以放在最前面get出來讓大家用
        memberDataSharePre = getSharedPreferences("memberDataPre", MODE_PRIVATE);
        String memberEmailDataCheck=memberDataSharePre.getString("email","查無資料");//取得登入後儲存的會員EMAIL
        Log.e("JSON", "會員EMAIL"+memberDataSharePre.getString("email","查無資料"));
        String memberNameDataCheck=memberDataSharePre.getString("name","查無資料");
        //如果SharedPreferance裡面的memberDataPre檔案裡的name沒有資料，就從網路下載會員資料
        if(memberNameDataCheck.equals("查無資料")) {
            //executorService = Executors.newSingleThreadExecutor();
            JSONObject packet = new JSONObject();
            try {
                JSONObject memberEmail = new JSONObject();
                memberEmail.put("email", memberEmailDataCheck);//抓出登入時儲存在SharedPreferance的會員EMAIL
                packet.put("memberEmail", memberEmail);

                Log.e("JSON", "這裡是從網路下載的會員資料");
                Toast.makeText(memberdataaPageActivity.this, "已送出EMAIL抓取會員資料", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //把email資料封裝成JSON格式 透過網路傳給Sever
            MediaType mType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(packet.toString(), mType);
            //VM IP=20.187.101.131
            Request request = new Request.Builder()
                    .url("http://192.168.43.21:8216/api/member/getMemberData")
                    .post(body)
                    .build();
            SimpleeAPIWorker apiCaller = new SimpleeAPIWorker(request, memberDataHandler);
            //產生Task準備給executor執行
            executorService.execute(apiCaller);

        }else{
            //直接從「透過SharedPreferance儲存在手機裡(Activity間共用)的"memberDataPre"檔案」撈出會員資料顯示
            String PointsData=memberDataSharePre.getString("points","查無資料");
            String nameData=memberDataSharePre.getString("name","查無資料");
            String emailData=memberDataSharePre.getString("email","查無資料");
            String phoneData=memberDataSharePre.getString("phone","查無資料");

            binding.memberPointsTX.setText(PointsData);
            binding.memberNameTT.setText(nameData);
            binding.memberEmailTX.setText(emailData);
            binding.memberPhoneTT.setText(phoneData);
            Log.e("JSON", "這裡是從SharePreferance取出的會員資料");
        }
        binding.toChangeMemberDataPageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(memberdataaPageActivity.this,MemberDataChangeActivity.class);
                startActivity(intent);
            }
        });

        binding.cancleChangememberData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }




    Handler memberDataHandler =new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            if(bundle.getInt("status")==123){
                Toast.makeText(memberdataaPageActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }else if(bundle.getInt("status")==465) {
                Toast.makeText(memberdataaPageActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }else if(bundle.getInt("status")==999) {
                //Toast.makeText(memberdataaPageActivity.this, bundle.getString("email"), Toast.LENGTH_LONG).show();
                binding.memberPointsTX.setText(bundle.getString("points"));
                binding.memberNameTT.setText(bundle.getString("name"));
                binding.memberEmailTX.setText(bundle.getString("email"));
                binding.memberPhoneTT.setText(bundle.getString("phone"));

                SharedPreferences.Editor editor=memberDataSharePre.edit();
                editor.putString("points",binding.memberPointsTX.getText().toString());
                editor.putString("name",binding.memberNameTT.getText().toString());
                editor.putString("email",binding.memberEmailTX.getText().toString());
                editor.putString("phone",binding.memberPhoneTT.getText().toString());
                editor.apply();
            }else{
                Toast.makeText(memberdataaPageActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }
        }
    };


    //Menu選單(右上角)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //製作Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        //用id判斷點了哪個選項
        if(id == R.id.membersetting){
            Intent intent=new Intent(memberdataaPageActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(memberdataaPageActivity.this,MyOrderActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(memberdataaPageActivity.this, MenuListActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(memberdataaPageActivity.this,HistoryOrderActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.myfavorite){
            Intent intent=new Intent(memberdataaPageActivity.this,MyFavoriteActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(memberdataaPageActivity.this,PointChangeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}