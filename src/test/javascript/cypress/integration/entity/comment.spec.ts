import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Comment e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/comments*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('comment');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Comments', () => {
    cy.intercept('GET', '/api/comments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('comment');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Comment').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Comment page', () => {
    cy.intercept('GET', '/api/comments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('comment');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('comment');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Comment page', () => {
    cy.intercept('GET', '/api/comments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('comment');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Comment');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Comment page', () => {
    cy.intercept('GET', '/api/comments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('comment');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Comment');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Comment', () => {
    cy.intercept('GET', '/api/comments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('comment');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Comment');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-10T19:54').invoke('val').should('equal', '2021-05-10T19:54');


    cy.get(`[data-cy="commentText"]`).type('Chair', { force: true }).invoke('val').should('match', new RegExp('Chair'));


    cy.get(`[data-cy="isOffensive"]`).should('not.be.checked');
    cy.get(`[data-cy="isOffensive"]`).click().should('be.checked');
    cy.setFieldSelectToLastOfEntity('appuser');

    cy.setFieldSelectToLastOfEntity('post');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/comments*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('comment');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Comment', () => {
    cy.intercept('GET', '/api/comments*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/comments/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('comment');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('comment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/comments*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('comment');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
