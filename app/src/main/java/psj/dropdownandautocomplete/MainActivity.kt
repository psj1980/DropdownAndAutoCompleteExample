package psj.dropdownandautocomplete

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.inputMethodManager
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast


class MainActivity : Activity() {

    val courseType = arrayOf("Starter", "Main course", "Dessert")
    val ingredients = setOf<String>("Mel", "Bagepulver", "Melis", "Sukker", "Mørbradkød", "Gær", "Æg", "Salt")
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeViewsUneditable()

        // Set button on click event listener
        button.onClick { if (edit) makeViewsUneditable() else makeViewsEditable() }

        // Listen for click on done button editText
        editText.setOnEditorActionListener { v, actionId, event -> if(actionId == EditorInfo.IME_ACTION_DONE) { toastNumber(); true } else false }

        // Listen for change of focus for editText
        editText.setOnFocusChangeListener { v, hasFocus -> if(!hasFocus) toastNumber() }


        // Autocomplete:
        val autoCompleteAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ingredients.toList())
        autoCompleteTextView.setAdapter(autoCompleteAdapter)


        // android.R.layout.simple_spinner_item
        // Spinner / Dropdown
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, courseType)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(1)
        // Herover kunne man have en getId(mainCourse: String) eller listen bør være en liste, hvor
        // man kan finde det ønskede id(element) via find.

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                toast(parent.getItemAtPosition(pos).toString())
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        })
    }

    fun toastNumber() {

        editText.setText(validateNumber(editText.text.toString()).toString())
        toast(editText.text.toString())
        hideKeyboard()
    }

    // Set attributes to enable editing in an EditText view
    fun makeViewsEditable() {

        edit = true

        autoCompleteTextView.isCursorVisible = true
        autoCompleteTextView.isClickable = true
        autoCompleteTextView.isFocusable = true
        autoCompleteTextView.isFocusableInTouchMode = true

        spinner.isEnabled = true

        editText.isCursorVisible = true
        editText.isClickable = true
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
    }

    // Set attributes to disable editing in an EditText view
    fun makeViewsUneditable() {

        edit = false
        autoCompleteTextView.isCursorVisible = false
        autoCompleteTextView.isClickable = false
        autoCompleteTextView.isFocusable = false
        autoCompleteTextView.isFocusableInTouchMode = false

        spinner.isEnabled = false

        editText.isCursorVisible = false
        editText.isClickable = false
        editText.isFocusable = false
        editText.isFocusableInTouchMode = false

        hideKeyboard()
    }

    fun hideKeyboard() {

        inputMethodManager.hideSoftInputFromWindow(exampleActivity.windowToken, 0)
    }

    fun validateNumber(number: String) = if (number.isEmpty() || number.toInt() < 1) 2 else number.toInt()
}
