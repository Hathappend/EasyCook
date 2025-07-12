package android.tugas.easycook.ui.detail;

import android.os.Bundle;
import android.tugas.easycook.data.response.Step;
import android.tugas.easycook.databinding.FragmentInstructionsBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class InstructionsFragment extends Fragment {

    private FragmentInstructionsBinding binding;
    private static final String ARG_STEPS = "steps_list";
    public static InstructionsFragment newInstance(List<Step> steps) {
        InstructionsFragment fragment = new InstructionsFragment();
        Bundle args = new Bundle();
        ArrayList<String> stepStrings = new ArrayList<>();
        for (Step step : steps) {
            stepStrings.add(step.getNumber() + ". " + step.getStep());
        }
        args.putStringArrayList(ARG_STEPS, stepStrings);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInstructionsBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            List<String> stepStrings = getArguments().getStringArrayList(ARG_STEPS);
            if (stepStrings != null) {
                StringBuilder sb = new StringBuilder();
                for (String step : stepStrings) {
                    sb.append(step).append("\n\n");
                }
                binding.tvInstructionsContent.setText(sb.toString());
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
