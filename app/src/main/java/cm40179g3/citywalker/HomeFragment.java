package cm40179g3.citywalker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.icu.util.Calendar;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import java.util.Locale;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.FrameLayout;
import android.widget.ImageView;



import androidx.annotation.NonNull;

import cm40179g3.citywalker.R;

public class HomeFragment extends Fragment {
    private Calendar selectedDate = null;

    private int currentSteps = 0;
    private int targetSteps = 0;
    private Calendar targetDate = null;
    private ProgressBar progressRing;
    private TextView tvStepCount;
    private TextView tvTarget;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button settingsButton = view.findViewById(R.id.btn_settings);
        settingsButton.setOnClickListener(v -> showSettingsDialog());
        progressRing = view.findViewById(R.id.progress_ring);
        tvStepCount = view.findViewById(R.id.tv_step_count);
        tvTarget = view.findViewById(R.id.tv_target);

        // Make the circle clickable
        FrameLayout circleContainer = (FrameLayout) view.findViewById(R.id.circle_background).getParent();
        circleContainer.setOnClickListener(v -> incrementStepCount());


        settingsButton.setOnClickListener(v -> showSettingsDialog());

        // Load saved state if available
        loadSavedState();

        return view;
    }

    private void incrementStepCount() {
        if (targetSteps == 0) {
            Toast.makeText(getContext(), "Please set a target first", Toast.LENGTH_SHORT).show();
            return;
        }

        currentSteps++;
        updateUI();

        // Save state
        saveState();
        Fragment shopFragment = getParentFragmentManager().findFragmentByTag("shop_fragment");
        if (shopFragment instanceof ShopFragment) {
            ((ShopFragment) shopFragment).incrementStepsCurrency();
        }
    }

    private void updateUI() {
        tvStepCount.setText(String.valueOf(currentSteps));

        tvStepCount.setText(String.valueOf(currentSteps));

        if (targetSteps > 0) {
            float progress = ((float) currentSteps / targetSteps) * 10000;
            progressRing.setProgress((int) progress); // Use setProgress instead
        } else {
            progressRing.setProgress(0);
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_walk_settings, null);

        // Initialize views
        EditText etTargetSteps = dialogView.findViewById(R.id.et_target_steps);
        Button btnPickDate = dialogView.findViewById(R.id.btn_pick_date);
        TextView tvSelectedDate = dialogView.findViewById(R.id.tv_selected_date);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);

        // Create dialog
        AlertDialog dialog = builder.setView(dialogView).create();

        // Date picker button click
        btnPickDate.setOnClickListener(v -> showDatePicker(tvSelectedDate));

        // Cancel button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Confirm button
        btnConfirm.setOnClickListener(v -> {
            String targetSteps = etTargetSteps.getText().toString();

            if (targetSteps.isEmpty() || selectedDate == null) {
                String errorMsg = targetSteps.isEmpty()
                        ? getString(R.string.enter_both_values)
                        : getString(R.string.no_date_selected);
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            saveWalkSettings(Integer.parseInt(targetSteps), selectedDate);
            dialog.dismiss();
        });

        dialog.show();
        // In the confirm button click listener, add:
        btnConfirm.setOnClickListener(v -> {
            String targetStepsStr = etTargetSteps.getText().toString();

            if (targetStepsStr.isEmpty() || selectedDate == null) {
                Toast.makeText(getContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            targetSteps = Integer.parseInt(targetStepsStr);
            targetDate = selectedDate;
            currentSteps = 0; // Reset counter when new target is set

            updateUI();
            saveState();
            dialog.dismiss();
        });
    }

    private void showDatePicker(TextView dateTextView) {
        Calendar calendar = selectedDate != null ? selectedDate : Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    String formattedDate = String.format("%d/%d/%d",
                            selectedDay, selectedMonth + 1, selectedYear);
                    dateTextView.setText(getString(R.string.date_selected, formattedDate));
                    dateTextView.setVisibility(View.VISIBLE);
                },
                year, month, day
        );

        // Set minimum date to today
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.show();
    }

    private void saveWalkSettings(int targetSteps, Calendar targetDate) {
        // Format the date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateStr = sdf.format(targetDate.getTime());

        // Save to SharedPreferences or ViewModel
        Toast.makeText(getContext(),
                "Settings saved: " + targetSteps + " steps by " + dateStr,
                Toast.LENGTH_SHORT).show();

        // Example using SharedPreferences:
        SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("target_steps", targetSteps);
        editor.putLong("target_date", targetDate.getTimeInMillis());
        editor.apply();
    }

    private void saveState() {
        SharedPreferences prefs = requireContext().getSharedPreferences("StepCounter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentSteps", currentSteps);
        editor.putInt("targetSteps", targetSteps);
        if (targetDate != null) {
            editor.putLong("targetDate", targetDate.getTimeInMillis());
        }
        editor.apply();
    }

    private void loadSavedState() {
        SharedPreferences prefs = requireContext().getSharedPreferences("StepCounter", Context.MODE_PRIVATE);
        currentSteps = prefs.getInt("currentSteps", 0);
        targetSteps = prefs.getInt("targetSteps", 0);
        long dateMillis = prefs.getLong("targetDate", 0);
        if (dateMillis > 0) {
            targetDate = Calendar.getInstance();
            targetDate.setTimeInMillis(dateMillis);
        }
        updateUI();
    }
}