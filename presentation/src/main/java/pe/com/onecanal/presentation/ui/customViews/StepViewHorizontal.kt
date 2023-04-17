package pe.com.onecanal.presentation.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import pe.com.onecanal.R
import pe.com.onecanal.databinding.ViewStepviewHorizontalBinding
import pe.com.onecanal.databinding.ViewStepviewItemBinding

class StepViewHorizontal @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private var stepContainerBinding: ViewStepviewHorizontalBinding =
        ViewStepviewHorizontalBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var stepItemBinding: ViewStepviewItemBinding
    private val linearLayoutContainer = stepContainerBinding.stepsContainer
    private val steepsArray: MutableList<ViewStepviewItemBinding> = ArrayList()
    private var numberOfSteps: Int = 1
    private var currentStepPosition: Int = 1
    private val filledCircle =
        ResourcesCompat.getDrawable(resources,R.drawable.bg_stepview_filled_circle, context.theme)
    private val outlinedCircle =
        ResourcesCompat.getDrawable(resources,R.drawable.bg_stepview_outlined_circle, context.theme)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StepViewHorizontal)
        numberOfSteps = a.getInt(R.styleable.StepViewHorizontal_number_of_steps, 1)
        currentStepPosition = a.getInt(R.styleable.StepViewHorizontal_set_step_at, 1)
        a.recycle()
        numberOfSteps = if (numberOfSteps <= 0) 1 else numberOfSteps
        setStepsNumber(numberOfSteps)
        setStepAt(currentStepPosition)
    }

    private fun setStepsNumber(steps_number: Int) {

        if (linearLayoutContainer.childCount > 0)
            linearLayoutContainer.removeAllViews()
        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        for (i in 0 until steps_number) {

            //SET THE NUMBER IN THE TEXTVIEW
            stepItemBinding =
                ViewStepviewItemBinding.inflate(LayoutInflater.from(context), this, false)
            stepItemBinding.tvNumber.text = (i + 1).toString()

            //FILL THE FIRST STEP CIRCLE
            if (i == 0)
            {
                stepItemBinding.circle.background = filledCircle
                stepItemBinding.tvNumber.setTextColor(ResourcesCompat.getColor(context.resources, R.color.white, null))
            }



            //REMOVE THE LAST STEPVIEWITEM LINE AND LEAVE THE CIRCLE ALONE
            // REMOVE WEIGHT 1 AND APPLY WRAP_CONTENT
            if (i == steps_number - 1) {
                stepItemBinding.line.visibility = GONE
                stepItemBinding.root.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            } else {
                stepItemBinding.root.layoutParams = params
            }
            //ADD STEVIEWITEM TO LINEAR LAYOUT
            steepsArray.add(stepItemBinding)

            linearLayoutContainer.addView(stepItemBinding.root)
        }

    }

    fun setStepAt(steps_at_number: Int) {
//DESPINTA  CUANDO RETROCEDE
        if(numberOfSteps>steps_at_number)
        {
            for (i in numberOfSteps-1 downTo steps_at_number step 1)
            {
                val item = steepsArray[i]
                item.circle.background = outlinedCircle
                steepsArray[i-1].line.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.input_text_stroke_color, null))
                item.tvNumber.setTextColor(ResourcesCompat.getColor(context.resources, R.color.input_text_stroke_color, null))
            }
        }


        val realStepAtNumber = steps_at_number - 1
        if (realStepAtNumber in 1 until numberOfSteps) {

            for (i in 0 until numberOfSteps) {
                if (i <= realStepAtNumber) {

                    val item = steepsArray[i]
                    item.circle.background = filledCircle

                    item.tvNumber.setTextColor(ResourcesCompat.getColor(context.resources, R.color.white, null))

                    if (i != realStepAtNumber)
                        item.line.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.orange, null))

                }
            }
        }
    }

}