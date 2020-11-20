package UnitOfWork;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasource.DBConnection;
import domain.Choice;
import domain.Exam;
import domain.ExamAnswer;
import domain.Question;
import domain.QuestionAnswer;
import mappers.ChoiceMapper;
import mappers.ExamAnswerMapper;
import mappers.ExamMapper;
import mappers.QuestionAnswerMapper;
import mappers.QuestionMapper;

public class UnitOfWork {
	private static ThreadLocal<UnitOfWork> current = new ThreadLocal<UnitOfWork>();
	
	private List<Object> newObjects = new ArrayList<>();
	private List<Object> dirtyObjects = new ArrayList<>();
	private List<Object> deletedObjects = new ArrayList<>();
	
	public static void newCurrent() {
		setCurrent(new UnitOfWork());
	}
	
	public static void setCurrent(UnitOfWork uow) {
		current.set(uow);
	}

	public static UnitOfWork getCurrent() {
		return (UnitOfWork) current.get();
	}
	
	public void registerNew(Object obj) {
		assert obj != null : "object is null";
		assert dirtyObjects.contains(obj) == false : "object is dirty";
		assert deletedObjects.contains(obj) == false : "object is deleted";
		assert newObjects.contains(obj) == false : "object is new";
		newObjects.add(obj);
	}
	
	public void registerDirty(Object obj) {
		assert obj != null : "object is null";
		assert deletedObjects.contains(obj) == false : "object is deleted";
		if (!dirtyObjects.contains(obj) && !newObjects.contains(obj)) {
			dirtyObjects.add(obj);
		}
	}
	
	public void registerDeleted(Object obj) {
		assert obj != null : "object is null";
		if (newObjects.remove(obj)) return;
		dirtyObjects.remove(obj);
		if (!deletedObjects.contains(obj)) {
			deletedObjects.add(obj);
		}
	}
	
	public void commit() throws Exception {
		Connection conn = DBConnection.getDBConnection();
		try {
			conn.setAutoCommit(false);
			for (Object obj : dirtyObjects) {
				if (obj instanceof ExamAnswer) {
					ExamAnswerMapper.update((ExamAnswer)obj, conn);
				}else if(obj instanceof QuestionAnswer) {
					QuestionAnswerMapper.update((QuestionAnswer)obj, conn);
				}else if (obj instanceof Question) {
					QuestionMapper.update((Question)obj);
				}else if (obj instanceof Exam) {
					ExamMapper.update((Exam)obj);
				}
			}
			
			for (Object obj : newObjects) {
				if (obj instanceof Exam) {
					ExamMapper.insert((Exam)obj);
				}else if(obj instanceof Question) {
					QuestionMapper.insert((Question)obj);
				}else if (obj instanceof Choice) {
					ChoiceMapper.insert((Choice)obj);
				}else if (obj instanceof ExamAnswer) {
					ExamAnswerMapper.insert((ExamAnswer) obj);
				}else if (obj instanceof QuestionAnswer) {
					QuestionAnswerMapper.insert((QuestionAnswer) obj);
				}
			}
	
			for (Object obj : deletedObjects) {
				if(obj instanceof Question) {
					QuestionMapper.delete((Question)obj);
				} else if (obj instanceof Choice) {
					ChoiceMapper.delete((Choice)obj);
				} else if (obj instanceof Exam) {
					ExamMapper.delete((Exam)obj);
				}
			}
			conn.commit();
		} catch (Exception e) {
			try {
				System.out.println("Rolling back transaction...");
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}