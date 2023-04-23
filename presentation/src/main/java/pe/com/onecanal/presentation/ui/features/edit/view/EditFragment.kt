package pe.com.onecanal.presentation.ui.features.edit.view

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.FragmentEditBinding
import pe.com.onecanal.databinding.FragmentProfileBinding
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.MaritalStatus
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.bindingTools.AutoCompleteTextView.bindAdapter
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.features.cuentaddactivitis.CuentasbancariasActivity
import pe.com.onecanal.presentation.ui.features.edit.intent.EditIntent
import pe.com.onecanal.presentation.ui.features.edit.intent.EditIntentConfig
import pe.com.onecanal.presentation.ui.features.edit.viewModel.EditViewModel
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.profile.adapter.BankLinearLayoutAdapter
import pe.com.onecanal.presentation.ui.features.profile.intent.ProfileIntent
import pe.com.onecanal.presentation.ui.features.profile.intent.ProfileIntentConfig
import pe.com.onecanal.presentation.ui.features.profile.view.ProfileFragmentDirections
import pe.com.onecanal.presentation.ui.features.profile.viewmodel.ProfileViewModel
import pe.com.onecanal.presentation.ui.features.splash.intent.SplashIntent

@AndroidEntryPoint
class EditFragment :
    BaseFragmentWithViewModel<FragmentEditBinding, EditViewModel>(),
    View.OnClickListener, EditIntentConfig.IntentCallback {


    var selectedType: Int? = null
    private lateinit var userToken: String
//    private val marital : MutableMap<String, Int?> = mutableMapOf()


    var position = 0

    override fun onCreateView(view: View) {
        super.onCreateView(view)


        binding.apply {
            appbar.tvTitle.text = getString(R.string.edit_profile)
            appbar.btnBack.setOnClickListener { goBack() }

            btnaddback.setOnClickListener{
//                Toast.makeText(context, "Hello, Kotlin!", Toast.LENGTH_SHORT).show()


                findNavController().navigate(EditFragmentDirections.actionEditFragmentToBankFragment3())

               // val navController = Navigation.findNavController(requireView())
                //val intent = Intent(activity, CuentasbancariasActivity::class.java)
                //startActivity(intent)


            }

            btnCancel.setOnClickListener {
                goBack()
            }
            btnSave.setOnClickListener {
                saveProfile()
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.intentState.collect {
                EditIntentConfig.instance(this@EditFragment).collect(it)
            }
        }
        getData()
    }

    override fun onResume() {
        super.onResume()
        //HIDE THE TOPBAR ACCORDING TO UI REQUIREMENTS
        (activity as MainDrawerActivity).showTopBar(false)
    }

    private fun saveProfile() {
        lifecycleScope.launchWhenCreated {
            viewModel.userIntent.send(EditIntent.SaveProfile(maritalType = selectedType/*marital[selectedType]*/))
        }
    }

    private fun getData() {
        lifecycleScope.launchWhenCreated {
            viewModel.userIntent.send(EditIntent.GetMaritalStatus)
            viewModel.userIntent.send(EditIntent.GetUserSession)
        }
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    override fun configureViewModel(): EditViewModel {
        val viewModel by viewModels<EditViewModel>()
        return viewModel
    }

    override val configureViewModelBindingVariable: Int  get() = BR.editViewModel

    override fun configureDataBinding(): FragmentEditBinding =
        FragmentEditBinding.inflate(layoutInflater)

    private fun adapterData(data : List<MaritalStatus>) {
        closeLoadingDialog()
        binding.autocompleteMarital.apply {
            bindAdapter(
                data.map { it.name },
                R.layout.list_popup_window_item,
                null
            )
            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, pos, _ ->
                    selectedType = data[pos].id
                }
        }
    }

    override fun onDoEditSuccess(userProfileData: UserProfileData?) {
        closeLoadingDialog()
        showMessageDialog(
            dialogType = MessageDialogType.Success,
            message = getString(R.string.txt_save_success),
            confirmationBtnTxt = getString(R.string.btn_save_success),
            isCancelable = false) {
            userProfileData!!.userToken = this@EditFragment.userToken
            lifecycleScope.launch {
                viewModel.userIntent.send(EditIntent.UpdateUserSession(userProfileData))
            }
        }
    }

    override fun onUserSessionDataFetched(userProfileData: UserProfileData?) {
        closeLoadingDialog()
        this@EditFragment.userToken = userProfileData!!.userToken
        printUserDataUI(userProfileData)
    }

    private fun printUserDataUI(user: UserProfileData?) {
        binding.apply {
            user?.let {
                this@EditFragment.userToken = it.userToken
                inputCompanyPosition.setText(user.businessJob)
                autocompleteMarital.setText(user.maritalName)
                inputAddress.setText(user.address)
                if(it.maritalId != null) selectedType = user.maritalId
                val e = user.salary == 0.0
                if(!e) inputMoney.setText(user.salary.toString())
                inputMoney.isEnabled = e
                inputMoney.isClickable = e
                inputMoney.isSelected = e
                viewModel.money.isEnabled(e)
            }

        }
    }

    override fun onGetMaritalStatus(data: List<MaritalStatus>) {
        adapterData(data)
    }

    override fun onUserSessionSaved() {
        goBack()
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        handleUseCaseFailureFromBase(error).message?.let {
            showMessageDialog(MessageDialogType.Error, it)
        }
    }

    override fun onClick(v: View?) {

    }
}