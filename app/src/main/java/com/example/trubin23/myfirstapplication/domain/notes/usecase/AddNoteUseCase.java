package com.example.trubin23.myfirstapplication.domain.notes.usecase;

import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomainModel;
import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomainModelMapper;
import com.example.trubin23.myfirstapplication.storage.model.Note;
import com.example.trubin23.myfirstapplication.storage.repository.NotesRepository;

public class AddNoteUseCase extends BaseUseCase<AddNoteUseCase.RequestValues, AddNoteUseCase.ResponseValues> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        NoteDomainModel noteDomainModel = requestValues.getNoteDomainModel();

        Note note = NoteDomainModelMapper.toDataModel(noteDomainModel);

        boolean result = NotesRepository.saveNote(note);

        if (result){
            getUseCaseCallback().onSuccess(new ResponseValues());
        } else {
            getUseCaseCallback().onError();
        }
    }

    public static class RequestValues implements BaseUseCase.RequestValues {

        private NoteDomainModel mNoteDomainModel;

        public RequestValues(NoteDomainModel noteDomainModel) {
            mNoteDomainModel = noteDomainModel;
        }

        NoteDomainModel getNoteDomainModel() {
            return mNoteDomainModel;
        }
    }

    public static class ResponseValues implements BaseUseCase.ResponseValues {

    }
}
