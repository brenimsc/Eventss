package com.example.events.ui

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.example.events.R
import com.example.events.data.model.Checkin
import com.example.events.data.model.Event
import com.example.events.data.model.State
import com.example.events.data.model.User
import com.example.events.databinding.ActivityDetailsBinding
import com.example.events.ui.constants.Constants.ID_EVENT
import com.example.events.ui.dialog.DialogSucessError
import com.example.events.ui.extensions.*
import com.example.events.ui.utils.Transformer
import com.example.events.ui.viewmodel.DetailsActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: DetailsActivityViewModel by viewModel()
    private lateinit var cardButtonAvancar: CardView
    private lateinit var layoutPrincipal: NestedScrollView
    private lateinit var layoutCheckin: CoordinatorLayout
    private lateinit var buttonRecarregar: Button
    private lateinit var layoutError: ConstraintLayout
    private lateinit var image: ImageView
    private lateinit var local: TextView
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var dateEvent: TextView
    private lateinit var price: TextView
    private lateinit var description: TextView
    private lateinit var titleEvent: TextView
    private lateinit var checkinLayoutName: TextInputLayout
    private lateinit var checkinLayoutEmail: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var name: TextInputEditText
    private lateinit var buttonCheckin: TextView
    private lateinit var cardCheckin: CardView
    private lateinit var markCheckin: TextView
    private lateinit var bottomSheet: BottomSheetBehavior<CardView>
    private lateinit var user: User
    private lateinit var event: Event
    private val idEvent by lazy {
        intent.extras?.get(ID_EVENT).toString()
    }
    private val progressDialog by lazy {
        ProgressDialog(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        observeUser()
        observeSnackbar()
        observeEvent()
        observeSucessCheckin()
        observeLoading()
        getUser()
        getEvent()

    }

    private fun getUser() {
        viewModel.getUser()
    }

    private fun getEvent() {
        idEvent?.let {
            viewModel.getEvent(it.toInt())
        }
    }

    private fun observeLoading() {
        viewModel.loading.observe(this) { loading ->
            if (loading) {
                progressDialog.showDialog()
            } else {
                progressDialog.hideDialog()
            }
        }
    }

    private fun observeSucessCheckin() {
        viewModel.sucessCheckin.observe(this) { sucees ->
            if (sucees) {
                hideCheckinShowMark()
            }

            DialogSucessError(this)
                .setSucess(sucees)
                .show()
        }
    }

    private fun observeEvent() {


        viewModel.event.observe(this) {
            when (it) {
                State.Loading -> {
                    hideLayoutError()
                    viewModel.showDialog()
                }

                is State.Error -> {
                    viewModel.hideDialog()
                    showLayoutError()
                }

                is State.Success -> {
                    viewModel.hideDialog()
                    hideLayoutError()
                    setupInfo(it.result)
                }

            }
        }
    }

    private fun observeSnackbar() {
        viewModel.snackbar.observe(this) {
            it?.let { error ->
                Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackBarShow()
            }
        }
    }

    private fun setupInfo(event: Event) {
        this.event = event
        titleEvent.text = event.title
        description.text = event.description
        price.text = getString(R.string.priceCheckin, event.price)
        dateEvent.text = event.date.formateDate(this)
        Glide.with(this)
            .load(event.image)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.img_sem_foto)
            .into(image)

        Transformer.getLocation(this, event.latitude, event.longitude) { street, _ ->
            local.text = street
        }

        topAppBar.menu.findItem(R.id.favorite)?.let {
            alterColorFavorites(it)
        }

        if (event.checkin) {
            hideCheckinShowMark()
        } else {
            layoutCheckin.showComponent()
        }

        cardButtonAvancar.setOnClickListener {
            if (setupErrorValidation()) return@setOnClickListener
            doCheckin()
            saveUser()
        }
    }

    private fun saveUser() {
        viewModel.saveUser(
            User(
                binding.txtUser.text.toString(),
                binding.txtEmail.text.toString()
            )
        )
    }

    private fun doCheckin() {
        viewModel.doCheckin(
            Checkin(
                idEvent.toInt(),
                binding.txtUser.text.toString(),
                binding.txtEmail.text.toString()
            )
        )
    }

    private fun observeUser() {
        viewModel.user.observe(this) {
            if (it != null) {
                user = it
            }
        }
    }

    private fun setupClickIconToolbar() {
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    if (this::event.isInitialized) {
                        setFavorite(menuItem)
                    }
                    true
                }
                R.id.share -> {
                    if (this::event.isInitialized) {
                        startActivity(
                            setupIntentShare()
                        )
                    }
                    true
                }
                else -> false
            }
        }

        topAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setFavorite(menuItem: MenuItem) {
        event.favorites = !event.favorites
        viewModel.alterToFavorite(event)
        alterColorFavorites(menuItem)
    }

    private fun setupIntentShare() = Intent.createChooser(Intent().apply {
        val message = "Sobre o evento ${event.title}, que ocorrerá ${
            event.date.formateDate(this@DetailsActivity)
        } : \n ${event.description}"

        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TITLE, "Compartilhar com: ")
        putExtra(
            Intent.EXTRA_TEXT,
            message
        )

        type = "text/plain"
    }, null)

    private fun showLayoutError() {
        layoutCheckin.hideComponent()
        layoutPrincipal.hideComponent()
        layoutError.showComponent()
    }

    private fun hideLayoutError() {
        layoutPrincipal.showComponent()
        layoutError.hideComponent()
        if (this::event.isInitialized) {
            if (this.event.checkin) layoutCheckin.showComponent() else layoutCheckin.hideComponent()
        }
    }

    private fun setupViews() {
        buttonRecarregar = binding.btnReload
        layoutCheckin = binding.checkinLayout
        layoutPrincipal = binding.containerDetail
        layoutError = binding.detailEventsErrorLayout
        markCheckin = binding.txtNameCheckin
        cardCheckin = binding.cardCheckin
        buttonCheckin = binding.txtCheckin
        name = binding.txtUser
        email = binding.txtEmail
        checkinLayoutEmail = binding.checkinLayoutEmail
        checkinLayoutName = binding.checkinLayoutUser
        titleEvent = binding.txtTitleDetail
        description = binding.txtDescriptionDetail
        price = binding.txtPriceDetail
        dateEvent = binding.txtDateDetail
        topAppBar = binding.topAppBar
        local = binding.txtLocalDetail
        image = binding.imgEventDetail
        cardButtonAvancar = binding.cardButtonAvancar
        bottomSheet = BottomSheetBehavior.from(cardCheckin)

        buttonRecarregar.setOnClickListener {
            viewModel.getEvent(idEvent.toInt())
        }

        setupClickIconToolbar()
        setupMandatoryField()
        setupCardBottomSheetCheckin()


    }

    private fun hideCheckinShowMark() {
        markCheckin.showComponent()
        layoutCheckin.hideComponent()
    }

    private fun alterColorFavorites(menuItem: MenuItem) {
        if (event.favorites) {
            setColorItemFavorites(menuItem, R.color.red)
        } else {
            setColorItemFavorites(menuItem, R.color.gray)
        }
    }

    private fun setColorItemFavorites(menuItem: MenuItem, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            menuItem.icon.setTint(ContextCompat.getColor(this, color))
            return
        }
        menuItem.icon.mutate().setColorFilter(
            ContextCompat.getColor(this, color),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun setupCardBottomSheetCheckin() {
        bottomSheet.apply {
            isHideable = false
        }
        buttonCheckin.setOnClickListener {
            showOrHide()
        }
    }

    private fun showOrHide() {
        when (bottomSheet.state) {
            BottomSheetBehavior.STATE_COLLAPSED -> {
                bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                if (this::user.isInitialized) {
                    binding.txtUser.setText(user.name)
                    binding.txtEmail.setText(user.email)
                }
            }
            else -> {
                bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setupErrorValidation(): Boolean {
        var hasErrorValidation = false


        if (name.text.isNullOrBlank()) {
            checkinLayoutName.error = getString(R.string.campoObrigatorio)
            checkinLayoutName.requestFocus()
            hasErrorValidation = true
        }

        if (email.text.isNullOrBlank()) {
            checkinLayoutEmail.error = getString(R.string.campoObrigatorio)
            checkinLayoutEmail.requestFocus()
            hasErrorValidation = true
        }

        if (!validateEmail(email.text.toString())) {
            checkinLayoutEmail.error = getString(R.string.emailIncorreto)
            checkinLayoutEmail.requestFocus()
            hasErrorValidation = true
        }
        return hasErrorValidation
    }

    private fun setupMandatoryField() {

        name.doAfterTextChanged {
            if (it!!.isNotEmpty()) {
                checkinLayoutName.error = null
                checkinLayoutName.isErrorEnabled = false
            } else {
                checkinLayoutName.error = getString(R.string.campoObrigatorio)
            }
        }

        binding.txtEmail.doAfterTextChanged {
            if (it!!.isNotEmpty()) {
                checkinLayoutEmail.error = null
                checkinLayoutEmail.isErrorEnabled = false

                if (!validateEmail(it.toString())) {
                    checkinLayoutEmail.error = getString(R.string.emailIncorreto)
                } else {
                    checkinLayoutEmail.error = null
                    checkinLayoutEmail.isErrorEnabled = false
                }
            } else {
                checkinLayoutEmail.error = getString(R.string.campoObrigatorio)
            }

        }
    }

}