
class LoginRepository{

    suspend fun login(username: String, password: String): LoginResponseModel{
        val request = LoginRequestModel(username,password)
        return RetrofitInstance.api.loginUser(request)
    }
}