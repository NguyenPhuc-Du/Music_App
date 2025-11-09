package com.example.sound4you.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.utils.ApiClient;
import com.example.sound4you.utils.ApiService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// (Đảm bảo bạn đã import đúng TrackAdapter, TrackItem, ItemClickListener)

public class ManageTrackFragment extends Fragment implements ItemClickListener {

    private static final String TAG = "ManageTrackFragment"; // Thêm TAG
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private List<TrackItem> trackItemList; // Đây là list cho UI
    private ApiService apiService; // Thêm ApiService

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo ApiService MỘT LẦN
        apiService = ApiClient.getService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_track, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ RecyclerView
        recyclerView = view.findViewById(R.id.manage_list_track);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo Adapter và List RỖNG
        trackItemList = new ArrayList<>();
        trackAdapter = new TrackAdapter(trackItemList);
        trackAdapter.setOnTrackActionListener(this);

        recyclerView.setAdapter(trackAdapter);

        // **XÓA** toàn bộ code tạo dữ liệu giả ở đây

        // 5. GỌI API ĐỂ LẤY DỮ LIỆU THẬT
        fetchTracks();
    }

    /**
     * Hàm này gọi API, lấy List<Track> và "chuyển đổi" nó
     * thành List<TrackItem> cho adapter.
     */
    private void fetchTracks() {
        Log.d(TAG, "Đang tải danh sách bài hát...");

        Call<List<Track>> call = apiService.getTracksWithUser();
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Track> apiTrackList = response.body(); // Dữ liệu từ API
                    Log.d(TAG, "Tải thành công " + apiTrackList.size() + " bài hát.");

                    trackItemList.clear(); // Xóa dữ liệu cũ

                    // 6. CHUYỂN ĐỔI (MAP) từ List<Track> sang List<TrackItem>
                    for (Track apiTrack : apiTrackList) {


                        // TODO: Chuyển đổi imageUrl (String) sang R.drawable (int)
                        // (Hiện tại, chúng ta tạm dùng ic_logo)
                        int imageResId = R.drawable.ic_logo;
                        Log.d("Track", "Title: " + apiTrack.getTitle() + " | Artist: " + (apiTrack.getArtist() != null ? apiTrack.getArtist() : "null"));


                        TrackItem uiItem = new TrackItem(
                                apiTrack.getId(),
                                apiTrack.getUser_id(),
                                apiTrack.getArtist(),
                                apiTrack.getArtistProfilePicture(),
                                apiTrack.getTitle(),
                                apiTrack.getAudio_url(),
                                apiTrack.getCover_url(),
                                apiTrack.getIs_verified()
                        );
                        System.out.println("Check: " + uiItem.getTitle());
                        trackItemList.add(uiItem);
                    }

                    // 7. Cập nhật RecyclerView
                    trackAdapter.notifyDataSetChanged();

                } else {
                    Log.e(TAG, "Tải thất bại. Code: " + response.code());
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Không thể tải danh sách bài hát", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.e(TAG, "Lỗi kết nối: " + t.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Các hàm click giữ nguyên, nhưng bạn nên
    // gọi API để cập nhật CSDL ở đây
    @Override
    public void onApproveClick(int position) {
        TrackItem clickedItem = trackItemList.get(position);
        int trackId = clickedItem.getId(); // Lấy ID thật từ TrackItem

        // Log để kiểm tra ID và URL
        Log.d(TAG, "Gửi approve cho trackId: " + trackId);

        Call<ResponseBody> call = apiService.approveTrack(trackId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Thành công -> cập nhật UI
                    clickedItem.setIs_verified(1);
                    trackAdapter.notifyItemChanged(position);

                    Toast.makeText(getContext(),
                            "Đã duyệt: " + clickedItem.getTitle(),
                            Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Approved thành công trackId: " + trackId);

                } else {
                    // Nếu server trả về 404 hoặc lỗi khác
                    Toast.makeText(getContext(),
                            "Lỗi: Không thể duyệt (Code " + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Lỗi approve. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Nếu mất kết nối hoặc không reach server
                Toast.makeText(getContext(),
                        "Lỗi kết nối mạng",
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure approve: " + t.getMessage());
            }
        });
    }



    @Override
    public void onDeleteClick(int position) {
        TrackItem clickedItem = trackItemList.get(position); // Lấy item hiện tại
        int trackId = clickedItem.getId();

        // 1. Gọi API xóa trên server
        Call<ResponseBody> call = apiService.deleteTrack(trackId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 2. Xóa khỏi list UI nếu server xóa thành công
                    trackItemList.remove(position);
                    trackAdapter.notifyItemRemoved(position);

                    Toast.makeText(getContext(), "Deleted: " + clickedItem.getTitle(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Deleted track ID: " + trackId);
                } else {
                    Toast.makeText(getContext(), "Lỗi: Không thể xóa track", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Lỗi delete. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Lỗi onFailure (delete): " + t.getMessage());
            }
        });
    }
}