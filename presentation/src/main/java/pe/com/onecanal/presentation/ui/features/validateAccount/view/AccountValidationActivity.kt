package pe.com.onecanal.presentation.ui.features.validateAccount.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.ActivityAccountValidateBinding
import pe.com.onecanal.domain.dtos.DataUserRes
import pe.com.onecanal.domain.dtos.DtoUsers
import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.ValidateData
import pe.com.onecanal.domain.utilsreniec.Contenido
import pe.com.onecanal.framework.hilt.modules.MessageDialojM
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
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AccountValidationActivity :
    BaseActivity<ActivityAccountValidateBinding, AccountValidationViewModel>(),
    View.OnClickListener, IntentCallback {
    private var termsAndConditions: String = ""
    private var contract: String = ""
    private var selectedDocumentType: String = ""
    private var telefonoingresado: String = ""
    private var correoingresado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureClickListeners()
        lifecycleScope.launchWhenCreated {
            getDocumentTypes()
            getTermsAndConditions()
            getContract()
        }
        createsSpannableText()


        iniciarCalendario();


    }


    private fun iniciarCalendario() {


        val txtfechanacimiento = binding.TxtFechaNacimiento;
        val txtcontainer = binding.TxtFechaNacimiento;


        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Crear un objeto Date a partir de los valores seleccionados
                val selectedDate = Date(year - 1900, monthOfYear, dayOfMonth)

                // Formatear la fecha utilizando SimpleDateFormat
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = sdf.format(selectedDate)


                txtfechanacimiento.setText(formattedDate)

            },
            year,
            month,
            day
        )




        txtcontainer.setOnClickListener {
            datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePickerDialog.show()
        }

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
                when (docType) {
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

        val documentNumber = binding.documentNumberEt.text.toString()
        val TxtFechaNacimiento = binding.TxtFechaNacimiento.text.toString()

        val userInfo = reniecboby(dni = documentNumber, birth_date = TxtFechaNacimiento)
        showLoadingDialog()
        apiService.getDataReniec(userInfo) {


            val objeto = it;
            closeLoadingDialog()

            if (objeto == null) {
                Toast.makeText(applicationContext, "No se encontró información", Toast.LENGTH_SHORT)
                    .show()
            } else {

                if (objeto.code == 200) {
                    showCustomDialog(objeto.data.UserReniec, documentNumber)
                } else if (objeto.code == 403) {

                    var me = MessageDialojM()
                    me.showMensajes(objeto.message, this)

                }

            }


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


    fun showCustomDialog(data: Contenido, dni: String) {

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.layout_reniec, null)

        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.show()

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)


        val txtnombres = dialog.findViewById<EditText>(R.id.txtnombres)
        val txtdireccion = dialog.findViewById<EditText>(R.id.txtdireccion)
        val txtfechanacimiento = dialog.findViewById<EditText>(R.id.txtfechanacimiento)
        val txtSexo = dialog.findViewById<EditText>(R.id.txtSexo)
        val TxtEstadoCivil = dialog.findViewById<EditText>(R.id.TxtEstadoCivil)
        val txtubigeo = dialog.findViewById<EditText>(R.id.txtubigeo)
        val containercorreo = dialog.findViewById<TextInputLayout>(R.id.containercorreo)
        val containertelefono = dialog.findViewById<TextInputLayout>(R.id.containertelefono)

        val txttelefono = dialog.findViewById<TextInputEditText>(R.id.txttelefno)
        val txtcorreo = dialog.findViewById<TextInputEditText>(R.id.txtcorreo)

        val acceptButton = dialog.findViewById<Button>(R.id.read_btn)
        val btnsalir = dialog.findViewById<Button>(R.id.btnsalir)
        val BtnCerrar = dialog.findViewById<ImageView>(R.id.BtnCerrar)

        txtnombres.setText(data.nombreCompleto)
        txtdireccion.setText(data.direccion)
        txtfechanacimiento.setText(data.fechaNacimiento)
        txtSexo.setText(data.sexo)
        TxtEstadoCivil.setText(data.estadoCivil)
        txtubigeo.setText(data.departamento + "-" + data.provincia + "-" + data.distrito + "")

        txtfechanacimiento.setOnClickListener {


        }

        acceptButton.setOnClickListener {


            if (txtcorreo.text.toString().length == 0) {
                containercorreo.error = "Ingrese Correo Electrónico";
                return@setOnClickListener
            } else {
                containercorreo.error = null;
            }

            if (txttelefono.text.toString().length == 0) {
                containertelefono.error = "Ingrese Teléfono";
                return@setOnClickListener
            } else {
                containertelefono.error = null;
            }

            telefonoingresado=txttelefono.text.toString();
            correoingresado=txtcorreo.text.toString();

            dialog.dismiss()

            IniciarActualizacion(data,dni)



        }

        btnsalir.setOnClickListener {

            dialog.dismiss()
        }

        BtnCerrar.setOnClickListener {

            dialog.dismiss()
        }



        dialog.show()
    }


    private fun IniciarActualizacion(data: Contenido,dni:String) {

        val apiService = RestApiService()

        val documentNumber = binding.documentNumberEt.text.toString()
        val TxtFechaNacimiento = binding.TxtFechaNacimiento.text.toString()

        val body = DtoUsers(
            id = "",
            documentType = selectedDocumentType,
            documentNumber = dni,
            birthDate = data.fechaNacimiento,
            names = data.nombres,
            surnames = data.apellidoPaterno+ " "+data.apellidoMaterno,
            email = correoingresado,
            province = data.provincia,
            city = data.distrito,
            sex = data.sexo,
            phone = telefonoingresado.toInt(),
            maritalStatusId = 1,
            address = data.direccionCompleta
        )
        showLoadingDialog()
        apiService.updateusers(body) {

            val objeto = it;


            if (objeto == null) {
                Toast.makeText(applicationContext, "No se encontró información", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (objeto.code == 200) {
                    Toast.makeText(applicationContext, "Validando cuenta...", Toast.LENGTH_SHORT)
                        .show()

                    lifecycleScope.launchWhenCreated {
                        viewModel.userIntent.send(
                            AccountValidationIntent.AccountValidation(
                                selectedDocumentType,
                                dni
                            )
                        )
                    }



                } else {
                    closeLoadingDialog()
                    var me = MessageDialojM()
                    me.showMensajes(objeto.message, this)
                }
            }
        }
    }


    private fun validateAccount() {
        val documentNumber = binding.documentNumberEt.text.toString()
        val TxtFechaNacimiento = binding.TxtFechaNacimiento.text.toString()

        val tipodocumentoval = binding.documentTypeBtn
        val fechanacimientoval = binding.fechacontainer
        val nuerodocval = binding.otDocumentNumber


        if (selectedDocumentType.length == 0) {
            tipodocumentoval.error = "Seleccione Tipo de Documento";
            return;
        } else {
            tipodocumentoval.error = null
        }

        if (documentNumber.length == 0) {
            nuerodocval.error = "Ingrese Número de Documento";
            return;
        } else {
            nuerodocval.error = null
        }

        if (TxtFechaNacimiento.length == 0) {
            fechanacimientoval.error = "Ingrese Fecha de nacimiento";
            return;
        } else {
            fechanacimientoval.error = null
        }

        getDataReniec()


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
        uiError.message?.let { showMessageDialog(MessageDialogType.Error, it) {} }

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

    override fun onAccountValidationSuccess(data: ValidateData) {
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