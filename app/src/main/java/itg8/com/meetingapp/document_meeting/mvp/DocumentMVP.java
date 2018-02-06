package itg8.com.meetingapp.document_meeting.mvp;

import java.util.List;

import itg8.com.meetingapp.db.TblDocument;

/**
 * Created by swapnilmeshram on 05/02/18.
 */

public interface DocumentMVP {
    public interface DocumentView{
        void onPreDocumentAvail(List<TblDocument> list);
        void onPostDocumentAvail(List<TblDocument> list);
        void onNoPreDocumentAvail();
        void onNoPostDocumentAvail();
    }

    public interface DocumentPresenter{
        void onStartGettingPreDocument();
        void onStartGettingPostDocument();
        void onAddPreDocument(TblDocument document);
        void onAddPostDocument(TblDocument document);
    }

    public interface DocumentListener{
        void onPostDocumentAvail(List<TblDocument> list);
        void onPreDocumentAvail(List<TblDocument> list);
        void onDocumentGatheringFail();
    }
}
