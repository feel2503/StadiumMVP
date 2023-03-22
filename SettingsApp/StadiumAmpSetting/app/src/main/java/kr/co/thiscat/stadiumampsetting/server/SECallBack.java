package kr.co.thiscat.stadiumampsetting.server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract public class SECallBack<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onResponseResult(response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        // 네트워크 접속 자체에서 실패한 경우 / 혹은 서버 접속 자체가 실패한 경우
        // 프로그래스를 사용할 경우 여기서 종료시킬 수 있어야 한다.
        // 해당 구조는 다시한번 생각해보도록 하자!!
        int aa = 0;
    }

    abstract public void onResponseResult(Response<T> response);
}
