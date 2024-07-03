package com.studymate154.studymate.HomeFragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.studymate154.studymate.*
import com.studymate154.studymate.MyPage.LogoutModel
import com.studymate154.studymate.MyPage.MyHeartPostActivity
import com.studymate154.studymate.MyPage.MyPostActivity
import com.studymate154.studymate.MyPage.PutMypageActivity
import com.studymate154.studymate.board.PostRetrofitAPI
import com.studymate154.studymate.databinding.FragmentMypageBinding
import com.studymate154.studymate.search.GetMatchingModel
import com.studymate154.studymate.signUp.SignUpResponseBody
import com.studymate154.studymate.signUp.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class MypageFragment : Fragment() {
    lateinit var binding : FragmentMypageBinding
    private lateinit var sharedPreferences: SharedPreferences
    val logoutModel = LogoutModel(null,null)

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data ?: return@registerForActivityResult
            val file = File(absolutelyPath(imageUri, requireContext()))
            Log.d("parkHwan",file.name)
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            Log.d("parkHwan",requestFile.toString())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            sendImage(body)

            binding.profileImage.setImageURI(imageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)


        //회원정보 로드
        getUser()

        //이미지 변경
        binding.profileImage.setOnClickListener {
            selectGallery()
        }

        //내 게시물로 이동
        binding.myPost.setOnClickListener {
            val intent = Intent(requireContext(), MyPostActivity::class.java)
            startActivity(intent)

        }

        //내 프로필 수정하기로 이동
        binding.putProfile.setOnClickListener {
            val intent = Intent(requireContext(), PutMypageActivity::class.java)
            startActivity(intent)
        }

        //내가 좋아요한 게시물로 이동
        binding.myHeartPost.setOnClickListener {
            val intent = Intent(requireContext(), MyHeartPostActivity::class.java)
            startActivity(intent)

        }

        //회원탈퇴
        binding.userDelete.setOnClickListener {
            showDeleteDialog()
        }

        //로그아웃
        binding.logout.setOnClickListener {
            showLogoutDialog()
        }




        return binding.root
    }

    private fun getUser() {
        val userToken = sharedPreferences.getString("userToken", "")
        val call = PostRetrofitAPI.emgMedService.getUserByEnqueue("Bearer $userToken")

        call.enqueue(object : Callback<GetMatchingModel> {
            override fun onResponse(call: Call<GetMatchingModel>, response: Response<GetMatchingModel>) {
                if (response.isSuccessful) {
                    val user = response.body()

                    val imageUrl = user?.imageUrl

                    Log.d("image",imageUrl.toString())
                    Glide.with(requireContext())
                        .load(imageUrl ?: R.drawable.mento_image)
                        .into(binding.profileImage)


                    // 나머지 유저 정보 설정
                    binding.nameText.text = user?.name
                    binding.matchingCount.text = user?.matchingCount.toString()
                    binding.reviewCount.text = user?.reviewCount.toString()

                }
            }
            override fun onFailure(call: Call<GetMatchingModel>, t: Throwable) {
                // Handle failure
            }
        })
    }

    //아이디 삭제
    private fun deleteUser() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.deleteUser("Bearer $userToken")

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(call: Call<SignUpResponseBody>, response: Response<SignUpResponseBody>) {
                if (response.isSuccessful) {
                    val user = response.body()
                }
            }
            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                // Handle failure
            }
        })
    }

    //로그아웃
    private fun logout() {
        val userToken = sharedPreferences.getString("userToken", "")
        val refreshToken = sharedPreferences.getString("refreshToken","")
        logoutModel.accessToken = userToken
        logoutModel.refreshToken = refreshToken
        val call = PostRetrofitAPI.emgMedService.postLogout("Bearer $userToken",logoutModel)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(call: Call<SignUpResponseBody>, response: Response<SignUpResponseBody>) {
                if (response.isSuccessful) {
                    val user = response.body()
                }
            }
            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                // Handle failure
            }
        })
    }

    //회원탈퇴 다이얼로그
    private fun showDeleteDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("회원 탈퇴")
        alertDialogBuilder.setMessage("정말 탈퇴하시겠습니까?")
        alertDialogBuilder.setPositiveButton("예") { dialog, _ ->
            deleteUser()
            dialog.dismiss()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("아니오") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    //로그아웃 다이얼로그
    private fun showLogoutDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("로그아웃")
        alertDialogBuilder.setMessage("로그아웃하시겠습니까?")
        alertDialogBuilder.setPositiveButton("예") { dialog, _ ->
            logout()
            dialog.dismiss()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("아니오") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    //갤러리 이미지 선택
    private fun selectGallery() {
        when (PackageManager.PERMISSION_GRANTED
        ) {
            // 갤러리 접근 권한이 있는 경우
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                val intent = Intent(Intent.ACTION_PICK)
                // 가져올 컨텐츠들 중에서 Image 만을 가져온다.
                intent.type = "image/*"
                // 갤러리에서 이미지를 선택한 후, 프로필 이미지뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용한다.
                imageResult.launch(intent)
            }
            // 권한 요청 하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
            else -> requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1000
            )
        }
    }

    // 절대경로 변환
    @SuppressLint("Recycle")
    private fun absolutelyPath(path: Uri?, context : Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        val result = c?.getString(index!!)

        return result!!
    }

    //이미지 put api
    private fun sendImage(body: MultipartBody.Part){
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.uploadImage("Bearer $userToken", body)

        call.enqueue(object: Callback<SignUpResponseBody>{
            override fun onResponse(call: Call<SignUpResponseBody>, response: Response<SignUpResponseBody>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(), "이미지 전송 성공", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "이미지 전송 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                Log.d("testt", t.message.toString())
            }

        })
    }


}