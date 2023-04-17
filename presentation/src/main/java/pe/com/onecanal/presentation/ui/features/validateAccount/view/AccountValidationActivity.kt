package pe.com.onecanal.presentation.ui.features.validateAccount.view

import android.app.Dialog
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.ActivityAccountValidateBinding
import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.ValidateData
import pe.com.onecanal.domain.utilsreniec.Contenido
import pe.com.onecanal.framework.hilt.utilsreniec.ReponseDNI
import pe.com.onecanal.framework.hilt.utilsreniec.RestApiService
import pe.com.onecanal.framework.hilt.utilsreniec.reniecboby
import pe.com.onecanal.presentation.ui.base.BaseActivity
import pe.com.onecanal.presentation.ui.dialogs.DocType
import pe.com.onecanal.presentation.ui.dialogs.DocumentConfirmationDialog
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.extensions.startActivityE
import pe.com.onecanal.presentation.ui.features.validateAccount.intent.AccountValidationIntent
import pe.com.onecanal.presentation.ui.features.validateAccount.intent.AccountValidationIntentConfig
import pe.com.onecanal.presentation.ui.features.validateAccount.intent.AccountValidationIntentConfig.IntentCallback
import pe.com.onecanal.presentation.ui.features.validateAccount.viewmodel.AccountValidationViewModel
import pe.com.onecanal.presentation.ui.features.validateCode.view.CodeValidationActivity
import pe.com.onecanal.presentation.ui.util.dataBinding


@AndroidEntryPoint
class AccountValidationActivity :
    BaseActivity<ActivityAccountValidateBinding, AccountValidationViewModel>(),
    View.OnClickListener, IntentCallback {
    private var termsAndConditions: String = ""
    private var contract: String = ""
    private var selectedDocumentType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureClickListeners()
        lifecycleScope.launchWhenCreated {
            getDocumentTypes()
            getTermsAndConditions()
            getContract()
        }
        createsSpannableText()
    }

    private fun configureClickListeners() {
        binding.apply {
            tvCancel.setOnClickListener(this@AccountValidationActivity)
            btnValidateAccount.setOnClickListener(this@AccountValidationActivity)
            documentTypeBtn.setOnClickListener(this@AccountValidationActivity)
            checkBoxIn.isEnabled = false
            checkBoxInContract.isEnabled = false
            checkBox.setOnClickListener {
                openTermsDialog(text = termsAndConditions, type = DocType.Terms)
            }
            checkBoxContract.setOnClickListener {
                openTermsDialog(text = contract, type = DocType.Contract)
            }
        }
    }

    private fun createsSpannableText() {
        val ss = SpannableString(getString(R.string.accept_terms_and_conditions))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {}
        }
        ss.setSpan(clickableSpan, 11, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.termsAndConditionsTv.text = ss
        binding.termsAndConditionsTv.movementMethod = LinkMovementMethod.getInstance()

        val ss2 = SpannableString(getString(R.string.accept_contract))
        ss2.setSpan(clickableSpan, 10, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.contractTv.text = ss2
        binding.contractTv.movementMethod = LinkMovementMethod.getInstance()
    }

    @Suppress("NON_EXHAUSTIVE_WHEN_STATEMENT")
    private fun openTermsDialog(text: String, type: DocType) {
        DocumentConfirmationDialog(
            terms = text,
            type = type
        ) { action, docType ->
            binding.apply {
                when(docType) {
                    DocType.Terms -> {
                        checkBoxIn.isChecked = action
                        btnValidateAccount.isEnabled = action && checkBoxInContract.isChecked
                    }
                    DocType.Contract -> {
                        checkBoxInContract.isChecked = action
                        btnValidateAccount.isEnabled = action && checkBoxIn.isChecked
                    }
                }

            }
        }.show(supportFragmentManager, "termsAndConditionsDialog")
    }

    override suspend fun handleIntentStates() {
        viewModel.intentState.collect {
            AccountValidationIntentConfig.instance(intentCallback = this).mainCollect(it)
        }
    }

    private suspend fun getTermsAndConditions() {
        viewModel.userIntent.send(AccountValidationIntent.GetTermsAndConditions)
    }

    private suspend fun getContract() {
        viewModel.userIntent.send(AccountValidationIntent.GetContract)
    }

    private suspend fun getDocumentTypes() {
        viewModel.userIntent.send(AccountValidationIntent.GetDocumentTypes)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_cancel -> finish()
            R.id.btn_validate_account -> validateAccount()
        }
    }

    fun getDataReniec() {
        val apiService = RestApiService()
        val userInfo = reniecboby(  numeroDNI = "70223123")

        apiService.getDataReniec(userInfo) {


            val jsonString = it.toString()
            val gson = Gson()
            val objeto = gson.fromJson(jsonString, ReponseDNI::class.java)
            showCustomDialog(objeto.Contenido)
            /*

            if (it?.exito == true) {
                // it = newly added user parsed as response
                // it?.id = newly added user ID

                Toast.makeText(this, "OK DATA", Toast.LENGTH_SHORT).show()
            } else {
                //Timber.d("Error registering new user")

                Toast.makeText(this, "ALGO FALLÓ", Toast.LENGTH_SHORT).show()
            }

             */
        }
    }


    fun showCustomDialog(data : Contenido ) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_reniec)

        val txtnombres = dialog.findViewById<EditText>(R.id.txtnombres)
        val txtdireccion = dialog.findViewById<EditText>(R.id.txtdireccion)
        val txxapellidos = dialog.findViewById<EditText>(R.id.txxapellidos)
        val acceptButton = dialog.findViewById<Button>(R.id.read_btn)
        val btnsalir = dialog.findViewById<Button>(R.id.btnsalir)

        txtnombres.setText(data.prenombres)
        txtdireccion.setText(data.direccion)
        txxapellidos.setText(data.apPrimer+" "+data.apSegundo)

        acceptButton.setOnClickListener {

            dialog.dismiss()

            lifecycleScope.launchWhenCreated {
                viewModel.userIntent.send(
                    AccountValidationIntent.AccountValidation(
                        selectedDocumentType,
                        "70223123"
                    )
                )
            }

        }

        btnsalir.setOnClickListener {

            dialog.dismiss()



        }

        dialog.show()
    }




    private fun validateAccount() {
        val documentNumber = binding.documentNumberEt.text.toString()

        getDataReniec();
        /*
        lifecycleScope.launchWhenCreated {
            viewModel.userIntent.send(
                AccountValidationIntent.AccountValidation(
                    selectedDocumentType,
                    documentNumber
                )
            )
        }

         */
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        val uiError = handleUseCaseFailureFromBase(error)
        uiError.message?.let { showMessageDialog(MessageDialogType.Error, it) {}}

    }

    override fun onDocumentTypesReady(data: List<DocumentType>) {
        closeLoadingDialog()
        val adapter = ArrayAdapter(this, R.layout.list_popup_window_item, data)
        binding.apply {
            //hide loader
            autocompleteDocumentTypes.setAdapter(adapter)
            autocompleteDocumentTypes.onItemClickListener =
                OnItemClickListener { _, _, pos, _ ->
                    selectedDocumentType = data[pos].name
                }
        }
    }

    override fun onAccountValidationSuccess(data : ValidateData) {
        closeLoadingDialog()
        showMessageDialog(
            MessageDialogType.Success,
            resources.getString(R.string.activation_email_sent),
            isCancelable = false
        ) {
            startActivityE<CodeValidationActivity>(data)
        }
    }

    override fun onTermsAndConditionsFetched(terms: String) {
        closeLoadingDialog()
        this.termsAndConditions = terms
    }

    override fun onContractFetched(doc: String) {
        this.contract = doc
    }

    override val dataBindingViewModel = BR.accountValidationViewModel
    override val viewModel: AccountValidationViewModel by viewModels()
    override val binding by dataBinding(ActivityAccountValidateBinding::inflate)
}