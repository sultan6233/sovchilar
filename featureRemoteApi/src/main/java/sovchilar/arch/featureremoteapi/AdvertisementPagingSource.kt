package sovchilar.arch.featureremoteapi

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import sovchilar.uz.domain.models.UserModel
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import java.io.IOException

class AdvertisementPagingSource(
    private val apiService: ApiService, private val gender: String
) : PagingSource<Int, AdvertisementsModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AdvertisementsModel> {
        val pageNumber = params.key ?: 1
        return try {
            val response = apiService.getAdvertisements(pageNumber, params.loadSize)
            if (response.isSuccessful && response.body() != null) {
                val userModel = response.body()!!
                val advertisements = userModel.personals
                val genderedAdvertisement: ArrayList<AdvertisementsModel> = ArrayList()
                advertisements.forEach {
                    if (it.gender == gender) {
                        genderedAdvertisement.add(it)
                    }
                }
                LoadResult.Page(
                    data = genderedAdvertisement,
                    prevKey = if (pageNumber == 1) null else pageNumber - 1,
                    nextKey = if (advertisements.isEmpty()) null else pageNumber + 1
                )
            } else {
                LoadResult.Error(Exception("Error fetching advertisements"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AdvertisementsModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}