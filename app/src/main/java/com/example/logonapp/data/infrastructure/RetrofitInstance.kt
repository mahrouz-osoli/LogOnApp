import com.example.logonapp.data.service.Login.Interface.ILoginService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance{

    private val  retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://oa.avreen.com:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: ILoginService by lazy{
        retrofit.create(ILoginService::class.java)
    }
}