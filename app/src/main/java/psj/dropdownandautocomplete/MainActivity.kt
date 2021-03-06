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
import java.text.NumberFormat


class MainActivity : Activity() {

    val courseType = arrayOf("Starter", "Main course", "Dessert")
    val ingredients = setOf<String>("Mel", "Bagepulver", "Melis", "Sukker", "Mørbradkød", "Gær", "Æg", "Salt")
    var edit = false
    val numberToCalculate = 4.409
    val noOfPeople = 2

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numberFormatter = NumberFormat.getInstance()
        numberFormatter.minimumFractionDigits = 0
        numberFormatter.maximumFractionDigits = 2

        editText.setText(noOfPeople.toString())
        textView4.text = numberFormatter.format(numberToCalculate)

        makeViewsUneditable()


        // Set button on click event listener
        button.onClick { if (edit) makeViewsUneditable() else makeViewsEditable() }



        // NUMBERS ONLY EditText:

        // Listen for click on done button in EditText
        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) updateTextViewToNewNumber()
            else false }

        // Listen for change of focus away from EditText
        editText.setOnFocusChangeListener { v, hasFocus -> if (!hasFocus) updateTextViewToNewNumber() }



        // AUTOCOMPLETE EditText:

        // Create an arrayAdapter and set autocomplete's adapter to the new adapter
        val autoCompleteAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ingredients.toList())
        autoCompleteTextView.setAdapter(autoCompleteAdapter)



        // SPINNER (Dropdown):

        // Create arrayAdapter with own spinner_item.xml
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, courseType)

        // Set spinner's adapter to new adapter and set index 1 as default choice
        spinner.adapter = spinnerAdapter
        spinner.setSelection(1) // get fra DB, eller en listes index

        // Listen for user selection in dropdown
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                toast(parent.getItemAtPosition(pos).toString())
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {
                //spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
    }

    // Find numberToCalculate in EditText and toast it
    fun updateTextViewToNewNumber(): Boolean {

        val numberFormatter = NumberFormat.getInstance()
        numberFormatter.minimumFractionDigits = 0
        numberFormatter.maximumFractionDigits = 2

        val newNoOfPeople = validateNumber(editText.text.toString())

        editText.setText(newNoOfPeople.toString())
        textView4.text = numberFormatter.format(calculateNewNumber(newNoOfPeople))
        hideKeyboard()
        return true
    }

    // Set attributes to enable editing in an EditText view
    fun makeViewsEditable() {

        edit = true
        autoCompleteTextView.isCursorVisible = true
        autoCompleteTextView.isClickable = true
        autoCompleteTextView.isFocusable = true
        autoCompleteTextView.isFocusableInTouchMode = true
        spinner.isEnabled = true
        button.text = "Done"
    }

    // Set attributes to disable editing in an EditText view
    fun makeViewsUneditable() {

        edit = false
        autoCompleteTextView.isCursorVisible = false
        autoCompleteTextView.isClickable = false
        autoCompleteTextView.isFocusable = false
        autoCompleteTextView.isFocusableInTouchMode = false
        spinner.isEnabled = false
        button.text = "Edit"
        hideKeyboard()
    }

    // Hide the keyboard
    fun hideKeyboard() { inputMethodManager.hideSoftInputFromWindow(exampleActivity.windowToken, 0) }

    // When the numberToCalculate-var is empty or has value < 1, 2 is returned, else return numberToCalculate
    fun validateNumber(number: String) = if (number.isEmpty() || number.toInt() < 1) 2 else number.toInt()

    fun calculateNewNumber(newNoOfPeople: Int) =
            if (newNoOfPeople == noOfPeople) numberToCalculate
            else (numberToCalculate / noOfPeople) * newNoOfPeople
}
