package com.example.sound4you;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.sound4you.ui.admin.AdminDashboardFragment;

/**
 * MainActivity là điểm vào chính của ứng dụng.
 * Nhiệm vụ duy nhất của nó là thiết lập layout chính và hiển thị Fragment đầu tiên.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // 1. Thiết lập layout chính của Activity, layout này chứa một "khung chứa" cho Fragment.
        setContentView(R.layout.fragment_container_view);

        // 2. Chỉ thực hiện việc thêm Fragment khi ứng dụng được tạo lần đầu (savedInstanceState == null).
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // 3. Tạo một thể hiện của "khung chứa" chính là AdminDashboardFragment.
            AdminDashboardFragment adminDashboardFragment = new AdminDashboardFragment();

            // 4. Thay thế nội dung của "khung chứa" trong activity_main.xml bằng AdminDashboardFragment.
            fragmentTransaction.replace(R.id.fragment_container_view, adminDashboardFragment);

            // 5. Hoàn tất giao dịch.
            fragmentTransaction.commit();
        }
    }
    // LƯU Ý: Toàn bộ code về RecyclerView, Adapter, và ItemClickListener đã được xóa khỏi đây
    // và sẽ được chuyển vào đúng Fragment của nó là ManageTrackFragment.
}
